package com.archu.budget.repository;

import com.archu.budget.entity.SavingsGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingsGoalRepository
        extends JpaRepository<SavingsGoal, Long> {
}
