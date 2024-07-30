package com.example.Wanted.Market.API.Payment.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private String paymentMethod; // 결제 방법 (예: KakaoPay, CreditCard 등)
    private Long amount; // 결제 금액
    private String orderId; // 주문 ID
    private String userId; // 사용자 ID
    private String returnUrl; // 결제 후 리디렉션 URL
    private String cancelUrl; // 결제 취소 후 리디렉션 URL
}
