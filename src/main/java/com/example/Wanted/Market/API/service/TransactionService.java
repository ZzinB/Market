package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Product;

import java.util.List;

public interface TransactionService {
    void approveTransaction(Long transactionId, String sellerUsername);
    List<Product> getPurchases(Long memberId);
    List<Product> getReservations(Long memberId);
}
