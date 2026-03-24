package com.archu.budget.controller;

import com.archu.budget.dto.MonthlySummaryResponse;
import com.archu.budget.dto.TransactionRequest;
import com.archu.budget.entity.Transaction;
import com.archu.budget.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1:5500")  // Allow frontend
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping("/ping")
    public String ping() {
        return "Backend is alive";
    }

    @PostMapping
    public Transaction addTransaction(
            @Valid @RequestBody TransactionRequest request) {
        return service.addTransaction(request);
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return service.getAllTransactions();
    }

    @PutMapping("/{id}")
    public Transaction updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request) {
        return service.updateTransaction(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        service.deleteTransaction(id);
    }

    @GetMapping("/summary/month")
    public MonthlySummaryResponse getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month) {
        return service.getMonthlySummary(year, month);
    }

    @GetMapping("/summary/year")
    public MonthlySummaryResponse getYearSummary(
            @RequestParam int year) {
        return service.getYearSummary(year);
    }

    @GetMapping("/paged")
    public Page<Transaction> getPagedTransactions(
            @RequestParam int page,
            @RequestParam int size) {
        return service.getPagedTransactions(page, size);
    }
}
