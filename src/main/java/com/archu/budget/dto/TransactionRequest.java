package com.archu.budget.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class TransactionRequest {

    @Positive(message = "Amount must be greater than 0")
    private double amount;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Type is required")
    private TransactionType type;

    @NotNull(message = "Date is required")
    private LocalDate date;   // 👈 NEW FIELD

    // getters and setters

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDate getDate() {   // 👈 NEW
        return date;
    }

    public void setDate(LocalDate date) {   // 👈 NEW
        this.date = date;
    }
}
