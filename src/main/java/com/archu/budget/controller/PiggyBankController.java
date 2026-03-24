package com.archu.budget.controller;

import com.archu.budget.dto.PiggyBankRequest;
import com.archu.budget.dto.PiggyBankResponse;
import com.archu.budget.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/piggybank")
@CrossOrigin(origins = "*")
public class PiggyBankController {

    private final TransactionService service;

    public PiggyBankController(TransactionService service) {
        this.service = service;
    }

    // CREATE GOAL
    @PostMapping
    public PiggyBankResponse createGoal(
            @Valid @RequestBody PiggyBankRequest request) {

        return service.createGoal(
                request.getTargetAmount(),
                request.getTargetDate()
        );
    }

    // GET CURRENT GOAL
    @GetMapping
    public PiggyBankResponse getGoal() {
        return service.getCurrentGoal();
    }
}
