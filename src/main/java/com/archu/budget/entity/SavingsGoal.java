package com.archu.budget.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "savings_goals")
public class SavingsGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double targetAmount;

    private LocalDate targetDate;

    private double savedSoFar;

    @Enumerated(EnumType.STRING)
    private GoalStatus status;

    private LocalDateTime completedAt;

    public SavingsGoal() {
        this.savedSoFar = 0.0;
        this.status = GoalStatus.ACTIVE;
    }

    // =========================
    // GETTERS & SETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public double getSavedSoFar() {
        return savedSoFar;
    }

    public void setSavedSoFar(double savedSoFar) {
        this.savedSoFar = savedSoFar;
    }

    public GoalStatus getStatus() {
        return status;
    }

    public void setStatus(GoalStatus status) {
        this.status = status;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
