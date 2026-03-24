package com.archu.budget.entity;

import com.archu.budget.dto.TransactionType;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    private String category;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private LocalDate date;   // 👈 ADD THIS

    // getters and setters

    public Long getId() {
        return id;
    }

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

    public LocalDate getDate() {   // 👈 ADD
        return date;
    }

    public void setDate(LocalDate date) {   // 👈 ADD
        this.date = date;
    }
}
