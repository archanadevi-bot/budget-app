package com.archu.budget.dto;

public class PiggyBankResponse {

    private double targetAmount;
    private double savedSoFar;
    private double remaining;
    private long daysLeft;
    private double progress;     // percentage
    private String message;
    private String status;       // ACTIVE, COMPLETED, EXPIRED

    public PiggyBankResponse(double targetAmount,
                             double savedSoFar,
                             double remaining,
                             long daysLeft,
                             double progress,
                             String message,
                             String status) {

        this.targetAmount = targetAmount;
        this.savedSoFar = savedSoFar;
        this.remaining = remaining;
        this.daysLeft = daysLeft;
        this.progress = progress;
        this.message = message;
        this.status = status;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public double getSavedSoFar() {
        return savedSoFar;
    }

    public double getRemaining() {
        return remaining;
    }

    public long getDaysLeft() {
        return daysLeft;
    }

    public double getProgress() {
        return progress;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
