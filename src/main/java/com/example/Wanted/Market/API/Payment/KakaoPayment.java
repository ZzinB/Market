package com.example.Wanted.Market.API.Payment;

import com.example.Wanted.Market.API.Payment.Paymentsystem.Payments;
import com.example.Wanted.Market.API.Payment.dto.Payment;
import com.example.Wanted.Market.API.Payment.dto.PaymentRequest;
import com.example.Wanted.Market.API.Payment.dto.PaymentResponse;
import com.example.Wanted.Market.API.Payment.dto.PaymentStatus;
import com.example.Wanted.Market.API.pay.kakaopay.KakaoPayProperties;
import com.example.Wanted.Market.API.pay.kakaopay.dto.KakaoApproveResponse;
import com.example.Wanted.Market.API.pay.kakaopay.dto.KakaoCancelResponse;
import com.example.Wanted.Market.API.pay.kakaopay.dto.KakaoReadyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 결제준비(ready)
 * 카카오페이 결제를 시작하기 위해 결제정보를 카카오페이 서버에 전달하고 결제 고유번호(TID)와 URL을 응답받는 단계입니다.
 *
 * Secret key를 헤더에 담아 파라미터 값들과 함께 POST로 요청합니다.
 * 요청이 성공하면 응답 바디에 JSON 객체로 다음 단계 진행을 위한 값들을 받습니다.
 * 서버(Server)는 tid를 저장하고, 클라이언트는 사용자 환경에 맞는 URL로 리다이렉트(redirect)합니다.
 */

/**
 * 1. 카카오페이 API 호출을 위한 헤더 설정
 * 2. 필수 파라미터 설정 및 POST 요청 전송
 * 3. 응답 처리 및 TID 저장
 */

@Component("KakaoPay")
@Slf4j
public class KakaoPayment implements Payments {

    private final KakaoPayProperties payProperties;
    private final PaymentRepository paymentRepository;
    private RestTemplate restTemplate = new RestTemplate();
    private KakaoReadyResponse kakaoReady;

    @Autowired
    public KakaoPayment(KakaoPayProperties payProperties, PaymentRepository paymentRepository) {
        this.payProperties = payProperties;
        this.paymentRepository = paymentRepository;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = "SECRET_KEY " + payProperties.getSecretKey();
        headers.set("Authorization", auth);
        headers.set("Content-Type", "application/json");
        return headers;
    }

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("cid", payProperties.getCid());
        parameters.put("partner_order_id", "ORDER_ID"); // 실제 주문 번호로 교체
        parameters.put("partner_user_id", "USER_ID");   // 실제 사용자 ID로 교체
        parameters.put("item_name", "ITEM_NAME");       // 실제 상품명으로 교체
        parameters.put("quantity", "1");                 // 수량, 숫자는 문자열로 전달
        parameters.put("total_amount", "2200");          // 총 금액, 숫자는 문자열로 전달
        parameters.put("vat_amount", "200");             // 부가세, 숫자는 문자열로 전달
        parameters.put("tax_free_amount", "0");          // 비과세 금액, 숫자는 문자열로 전달
        parameters.put("approval_url", "http://localhost:8080/success");
        parameters.put("fail_url", "http://localhost:8080/fail");
        parameters.put("cancel_url", "http://localhost:8080/cancel");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        kakaoReady = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                requestEntity,
                KakaoReadyResponse.class);

        PaymentResponse response = new PaymentResponse();
        response.setTransactionId(kakaoReady.getTid());
        response.setPaymentUrl(kakaoReady.getNext_redirect_pc_url());
        response.setStatus("READY");
        response.setMessage("KakaoPay payment ready");

        return response;

    }

    @Override
    public PaymentStatus checkPaymentStatus(String transactionId) {
        // 카카오페이 요청
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", payProperties.getCid());
        parameters.put("tid", transactionId);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        ResponseEntity<KakaoApproveResponse> responseEntity = restTemplate.exchange(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                HttpMethod.POST,
                requestEntity,
                KakaoApproveResponse.class);

        KakaoApproveResponse statusResponse = responseEntity.getBody();

        PaymentStatus paymentStatus = new PaymentStatus();
        paymentStatus.setTransactionId(statusResponse.getTid());

        // 결제 승인 시간(approved_at)을 통해 결제 상태 판단
        if (statusResponse.getApproved_at() != null) {
            paymentStatus.setStatus("SUCCESS");
        } else {
            paymentStatus.setStatus("PENDING");
        }

        return paymentStatus;
    }

    @Override
    public void cancelPayment(String transactionId) {
        // 1. 결제 정보를 데이터베이스에서 조회
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction ID: " + transactionId));

        // 2. 환불 금액 설정
        String cancelAmount = payment.getAmount().toString(); // 실제 환불 금액

        // 3. 환불 요청을 위한 파라미터 설정
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", payProperties.getCid());
        parameters.put("tid", transactionId);
        parameters.put("cancel_amount", cancelAmount); // 실제 환불 금액
        parameters.put("cancel_tax_free_amount", "0"); // 비과세 금액
        parameters.put("cancel_vat_amount", "0"); // 부가세

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        ResponseEntity<KakaoCancelResponse> responseEntity = restTemplate.exchange(
                "https://open-api.kakaopay.com/online/v1/payment/cancel",
                HttpMethod.POST,
                requestEntity,
                KakaoCancelResponse.class);

        KakaoCancelResponse cancelResponse = responseEntity.getBody();

        if (cancelResponse == null || cancelResponse.getStatus() == null) {
            throw new RuntimeException("Refund failed or response is invalid");
        }

        // 환불 처리 결과 확인
        if ("CANCEL_SUCCESS".equals(cancelResponse.getStatus())) {
            log.info("Refund successful for transaction ID: " + transactionId);
        } else {
            String errorMessage = "Refund failed with status: " + cancelResponse.getStatus();
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }
    }
}