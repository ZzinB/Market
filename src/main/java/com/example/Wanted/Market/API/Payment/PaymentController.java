package com.example.Wanted.Market.API.Payment;

import com.example.Wanted.Market.API.Payment.dto.PaymentRequest;
import com.example.Wanted.Market.API.Payment.dto.PaymentResponse;
import com.example.Wanted.Market.API.Payment.dto.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    //결제 요청
    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayments(@RequestBody PaymentRequest request){
        PaymentResponse response = paymentService.processPayment(request);
        return ResponseEntity.ok(response);
    }

    //결제 상태 확인
    @GetMapping("/status/{transactionsId}")
    public ResponseEntity<PaymentStatus> checkPaymentStatus(@PathVariable String transactionsId){
        PaymentStatus status = paymentService.checkPaymentStatus(transactionsId);
        return ResponseEntity.ok(status);
    }

    //결제 취소
    @PostMapping("/{transactionId}/cancel")
    public ResponseEntity<String> cancelPayment(@PathVariable String transactionId){
        paymentService.cancelPayment(transactionId);
        return ResponseEntity.ok("결제 취소 성공");
    }
}
