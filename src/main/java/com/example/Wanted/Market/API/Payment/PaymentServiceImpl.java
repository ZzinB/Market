package com.example.Wanted.Market.API.Payment;

import com.example.Wanted.Market.API.Payment.Paymentsystem.PaymentContext;
import com.example.Wanted.Market.API.Payment.Paymentsystem.Payments;
import com.example.Wanted.Market.API.Payment.dto.Payment;
import com.example.Wanted.Market.API.Payment.dto.PaymentRequest;
import com.example.Wanted.Market.API.Payment.dto.PaymentResponse;
import com.example.Wanted.Market.API.Payment.dto.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService{
    private final PaymentContext paymentContext;
    private final PaymentRepository paymentRepository;


    @Autowired
    public PaymentServiceImpl(PaymentContext paymentContext, PaymentRepository paymentRepository) {
        this.paymentContext = paymentContext;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        Payments paymentSystem = paymentContext.getPaymentSystem(request.getPaymentMethod());
        return paymentSystem.processPayment(request);
    }

    @Override
    @Transactional
    public PaymentStatus checkPaymentStatus(String transactionId) {
        // 결제 정보를 Optional<Payment>으로 조회
        Optional<Payment> optionalPayment = paymentRepository.findByTransactionId(transactionId);
        Payment payment = optionalPayment.orElseThrow(() ->
                new IllegalArgumentException("Payment not found with transactionId: " + transactionId)
        );
        Payments paymentSystem = paymentContext.getPaymentSystem(payment.getPaymentMethod());
        return paymentSystem.checkPaymentStatus(transactionId);
    }

    @Override
    @Transactional
    public void cancelPayment(String transactionId) {
        Optional<Payment> optionalPayment = paymentRepository.findByTransactionId(transactionId);
        Payment payment = optionalPayment.orElseThrow(() ->
                new IllegalArgumentException("Payment not found with transactionId: " + transactionId)
        );
        Payments paymentSystem = paymentContext.getPaymentSystem(payment.getPaymentMethod());
        paymentSystem.cancelPayment(transactionId);
        payment.setPaymentStatus("CANCELLED");
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
    }


    //결제 시스템
    private Payments getPaymentSystem(String paymentMethod){
        switch (paymentMethod){
            case "KakaoPay":
                return paymentContext.getPaymentSystem("KakaoPay");
            default:
                throw new IllegalArgumentException("Unknown payment method: " + paymentMethod);
        }
    }
}
