package com.archu.budget.dto;

public class MonthlySummaryResponse {

    private double totalIncome;
    private double totalExpense;
    private double balance;
    private boolean deficit;
    private String message;

    public MonthlySummaryResponse(double totalIncome,
                                  double totalExpense,
                                  double balance,
                                  boolean deficit,
                                  String message) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.balance = balance;
        this.deficit = deficit;
        this.message = message;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isDeficit() {
        return deficit;
    }

    public String getMessage() {
        return message;
    }
}
