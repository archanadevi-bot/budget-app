package com.archu.budget.service;

import com.archu.budget.dto.MonthlySummaryResponse;
import com.archu.budget.dto.PiggyBankResponse;
import com.archu.budget.dto.TransactionRequest;
import com.archu.budget.dto.TransactionType;
import com.archu.budget.entity.GoalStatus;
import com.archu.budget.entity.SavingsGoal;
import com.archu.budget.entity.Transaction;
import com.archu.budget.repository.SavingsGoalRepository;
import com.archu.budget.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final SavingsGoalRepository goalRepository;

    public TransactionService(TransactionRepository repository,
                              SavingsGoalRepository goalRepository) {
        this.repository = repository;
        this.goalRepository = goalRepository;
    }

    // =========================
    // CREATE TRANSACTION
    // =========================
    public Transaction addTransaction(TransactionRequest request) {

        Transaction tx = new Transaction();
        tx.setAmount(request.getAmount());
        tx.setCategory(request.getCategory());
        tx.setType(request.getType());
        tx.setDate(request.getDate());

        Transaction savedTransaction = repository.save(tx);

        Optional<SavingsGoal> optionalGoal =
                goalRepository.findAll().stream().findFirst();

        optionalGoal.ifPresent(goal -> {

            double updatedSavedAmount = goal.getSavedSoFar();

            if (request.getType() == TransactionType.INCOME &&
                    goal.getStatus() == GoalStatus.ACTIVE) {

                double contribution = request.getAmount() * 0.30;
                updatedSavedAmount += contribution;
                updatedSavedAmount =
                        Math.round(updatedSavedAmount * 100.0) / 100.0;

                goal.setSavedSoFar(updatedSavedAmount);

                if (updatedSavedAmount >= goal.getTargetAmount()) {
                    goal.setStatus(GoalStatus.COMPLETED);
                    goal.setCompletedAt(java.time.LocalDateTime.now());
                }

                goalRepository.save(goal);
            }
        });

        return savedTransaction;
    }

    // =========================
    // READ ALL
    // =========================
    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    // =========================
    // UPDATE
    // =========================
    public Transaction updateTransaction(Long id, TransactionRequest request) {

        Transaction existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        existing.setAmount(request.getAmount());
        existing.setCategory(request.getCategory());
        existing.setType(request.getType());
        existing.setDate(request.getDate());

        return repository.save(existing);
    }

    // =========================
    // DELETE
    // =========================
    public void deleteTransaction(Long id) {
        repository.deleteById(id);
    }

    // =========================
    // CREATE GOAL
    // =========================
    public PiggyBankResponse createGoal(double targetAmount,
                                        LocalDate targetDate) {

        if (targetAmount <= 0) {
            throw new RuntimeException("Target amount must be greater than 0");
        }

        if (targetDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("Target date must be in the future");
        }

        goalRepository.deleteAll();

        SavingsGoal goal = new SavingsGoal();
        goal.setTargetAmount(targetAmount);
        goal.setTargetDate(targetDate);
        goal.setSavedSoFar(0);
        goal.setStatus(GoalStatus.ACTIVE);

        goalRepository.save(goal);

        return analyzeGoal(goal);
    }

    // =========================
    // GET CURRENT GOAL
    // =========================
    public PiggyBankResponse getCurrentGoal() {

        Optional<SavingsGoal> optionalGoal =
                goalRepository.findAll().stream().findFirst();

        if (optionalGoal.isEmpty()) {
            return null;
        }

        return analyzeGoal(optionalGoal.get());
    }

    // =========================
    // ANALYZE GOAL
    // =========================
    public PiggyBankResponse analyzeGoal(SavingsGoal goal) {

        double remaining =
                goal.getTargetAmount() - goal.getSavedSoFar();

        long daysLeft = Math.max(0,
                ChronoUnit.DAYS.between(LocalDate.now(),
                        goal.getTargetDate()));

        double progress =
                (goal.getSavedSoFar() / goal.getTargetAmount()) * 100;

        progress = Math.max(0, Math.min(progress, 100));
        progress = Math.round(progress * 100.0) / 100.0;

        if (goal.getStatus() == GoalStatus.ACTIVE && daysLeft <= 0) {
            goal.setStatus(GoalStatus.EXPIRED);
            goalRepository.save(goal);
        }

        String message;

        if (goal.getStatus() == GoalStatus.COMPLETED) {
            message = "Goal achieved! You managed your savings wisely.";
        }
        else if (goal.getStatus() == GoalStatus.EXPIRED) {
            message = "Goal expired. Consider setting a realistic timeline.";
        }
        else if (progress < 20) {
            message = "You're just getting started. Stay consistent.";
        }
        else if (progress < 50) {
            message = "Good progress. Keep building momentum.";
        }
        else if (progress < 90) {
            message = "Strong discipline. You're getting close.";
        }
        else {
            message = "Almost there! Finish strong.";
        }

        return new PiggyBankResponse(
                goal.getTargetAmount(),
                goal.getSavedSoFar(),
                remaining,
                daysLeft,
                progress,
                message,
                goal.getStatus().name()
        );
    }

    // =========================
    // MONTHLY SUMMARY (FIXED DEFICIT LOGIC)
    // =========================
    public MonthlySummaryResponse getMonthlySummary(int year, int month) {

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        List<Transaction> transactions =
                repository.findByDateBetween(start, end);

        double totalIncome = 0;
        double totalExpense = 0;

        for (Transaction t : transactions) {
            if (t.getType() == TransactionType.INCOME) {
                totalIncome += t.getAmount();
            } else {
                totalExpense += t.getAmount();
            }
        }

        double balance = totalIncome - totalExpense;
        boolean deficit = balance < 0;

        String message;

        if (totalIncome == 0 && totalExpense == 0) {
            message = "No transactions this month.";
        } else if (deficit) {
            message = "You are running a deficit this month.";
        } else if (balance < totalIncome * 0.2) {
            message = "Savings are low. Try to control spending.";
        } else {
            message = "Good financial control. Keep saving.";
        }

        return new MonthlySummaryResponse(
                totalIncome,
                totalExpense,
                Math.abs(balance),
                deficit,
                message
        );
    }

    // =========================
    // YEAR SUMMARY (FIXED DEFICIT LOGIC)
    // =========================
    public MonthlySummaryResponse getYearSummary(int year) {

        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        List<Transaction> transactions =
                repository.findByDateBetween(start, end);

        double totalIncome = 0;
        double totalExpense = 0;

        for (Transaction t : transactions) {
            if (t.getType() == TransactionType.INCOME) {
                totalIncome += t.getAmount();
            } else {
                totalExpense += t.getAmount();
            }
        }

        double balance = totalIncome - totalExpense;
        boolean deficit = balance < 0;

        String message;

        if (totalIncome == 0 && totalExpense == 0) {
            message = "No transactions this year.";
        } else if (deficit) {
            message = "You overspent this year.";
        } else {
            message = "Year looks financially stable.";
        }

        return new MonthlySummaryResponse(
                totalIncome,
                totalExpense,
                Math.abs(balance),
                deficit,
                message
        );
    }

    // =========================
    // PAGINATION
    // =========================
    public Page<Transaction> getPagedTransactions(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }
}
