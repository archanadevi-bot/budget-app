package com.archu.budget.controller;

import com.archu.budget.dto.MonthlySummaryResponse;
import com.archu.budget.service.TransactionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summary")
@CrossOrigin
public class SummaryController {

    private final TransactionService service;

    public SummaryController(TransactionService service) {
        this.service = service;
    }

    @GetMapping("/month")
    public MonthlySummaryResponse getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month) {

        return service.getMonthlySummary(year, month);
    }

    @GetMapping("/year")
    public MonthlySummaryResponse getYearSummary(
            @RequestParam int year) {

        return service.getYearSummary(year);
    }
}
