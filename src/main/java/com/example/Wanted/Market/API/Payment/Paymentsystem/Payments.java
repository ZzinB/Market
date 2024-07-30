package com.example.Wanted.Market.API.Payment.Paymentsystem;

import com.example.Wanted.Market.API.Payment.dto.PaymentRequest;
import com.example.Wanted.Market.API.Payment.dto.PaymentResponse;
import com.example.Wanted.Market.API.Payment.dto.PaymentStatus;

public interface Payments {
    PaymentResponse processPayment(PaymentRequest request);
    PaymentStatus checkPaymentStatus(String transactionId);
    void cancelPayment(String transactionId);
}
