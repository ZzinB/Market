package com.example.Wanted.Market.API.repository;

import com.example.Wanted.Market.API.domain.Item;
import com.example.Wanted.Market.API.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
