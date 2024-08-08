package com.example.Wanted.Market.API.Payment.dto;

import lombok.Data;

@Data
public class PaymentResponse {
    private String transactionId; // 결제 고유 번호
    private String paymentUrl; // 결제 페이지 URL (결제 진행 시 사용)
    private String status; // 결제 요청 상태 (예: 요청 성공, 요청 실패 등)
    private String message; // 상태 메시지 (예: 오류 메시지, 성공 메시지 등)

    // 기본 생성자
    public PaymentResponse() {
    }

    // 매개변수를 받는 생성자
    public PaymentResponse(String transactionId) {
        this.transactionId = transactionId;
    }
}

