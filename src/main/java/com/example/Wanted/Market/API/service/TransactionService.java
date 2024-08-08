package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.exception.MemberNotFoundException;
import com.example.Wanted.Market.API.exception.ProductNotFoundException;
import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.domain.Product;
import com.example.Wanted.Market.API.domain.Transaction;
import com.example.Wanted.Market.API.repository.MemberRepository;
import com.example.Wanted.Market.API.repository.ProductRepository;
import com.example.Wanted.Market.API.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    public Transaction createTransaction(Long productId, Long sellerId, Long buyerId, String status) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Member seller = memberRepository.findById(sellerId)
                .orElseThrow(() -> new MemberNotFoundException(sellerId));

        Member buyer = memberRepository.findById(buyerId)
                .orElseThrow(() -> new MemberNotFoundException(buyerId));

        Transaction transaction = new Transaction();
        transaction.setProduct(product);
        transaction.setSeller(seller);
        transaction.setBuyer(buyer);
        transaction.setStatus(status);

        return transactionRepository.save(transaction);
    }
}
