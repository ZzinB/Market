package com.example.Wanted.Market.API.repository;

import com.example.Wanted.Market.API.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}

