package com.example.Wanted.Market.API.Payment.Paymentsystem;

import com.example.Wanted.Market.API.Payment.dto.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentContext {
    private final Map<String, Payments> paymentSystems;

    @Autowired
    public PaymentContext(Map<String, Payments> paymentSystems){
        this.paymentSystems = paymentSystems;
    }

    public Payments getPaymentSystem(String paymentMethod){
        Payments paymentSystem = paymentSystems.get(paymentMethod);
        if(paymentSystem == null){
            throw new IllegalArgumentException("Unknown payment method: " + paymentMethod);
        }
        return paymentSystem;
    }
}
