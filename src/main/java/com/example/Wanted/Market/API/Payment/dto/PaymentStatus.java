package com.example.Wanted.Market.API.Payment.dto;

import lombok.Data;

@Data
public class PaymentStatus {
    private String transactionId; // 결제 고유 번호
    private String status; // 결제 상태 (예: 성공, 실패, 대기 중 등)
    private Long amount; // 결제 금액
    private String message; // 상태 메시지
}
