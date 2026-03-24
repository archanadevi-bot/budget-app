package com.archu.budget.repository;

import com.archu.budget.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    // Get transactions between two dates
    List<Transaction> findByDateBetween(LocalDate start, LocalDate end);
}
