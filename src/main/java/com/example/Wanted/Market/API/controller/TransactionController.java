package com.example.Wanted.Market.API.controller;

import com.example.Wanted.Market.API.domain.Product;
import com.example.Wanted.Market.API.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/{transactionId}/approve")
    public ResponseEntity<?> approveTransaction(@PathVariable Long transactionId, Principal principal) {
        transactionService.approveTransaction(transactionId, principal.getName());
        return ResponseEntity.ok("거래 승인 성공");
    }

    @GetMapping("/members/{memberId}/purchases")
    public ResponseEntity<List<Product>> getPurchases(@PathVariable Long memberId) {
        return ResponseEntity.ok(transactionService.getPurchases(memberId));
    }

    @GetMapping("/members/{memberId}/reservations")
    public ResponseEntity<List<Product>> getReservations(@PathVariable Long memberId) {
        return ResponseEntity.ok(transactionService.getReservations(memberId));
    }
}

