package com.example.Wanted.Market.API.pay.kakaopay;

import com.example.Wanted.Market.API.pay.kakaopay.dto.KakaoApproveResponse;
import com.example.Wanted.Market.API.pay.kakaopay.dto.KakaoCancelResponse;
import com.example.Wanted.Market.API.pay.kakaopay.dto.KakaoReadyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KakaoPayService {

    private final KakaoPayProperties payProperties;
    private RestTemplate restTemplate = new RestTemplate();
    private KakaoReadyResponse kakaoReady;


    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = "SECRET_KEY " + payProperties.getSecretKey();
        headers.set("Authorization", auth);
        headers.set("Content-Type", "application/json");
        return headers;
    }

    /**
     * 결제 완료 승인
     */
    public KakaoReadyResponse kakaoPayReady() {
            //MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
            Map<String, Object> parameters = new HashMap<>();
//            parameters.put("cid", payProperties.getCid());
//            parameters.put("partner_order_id", "가맹점 주문 번호");
//            parameters.put("partner_user_id", "가맹점 회원 ID");
//            parameters.put("item_name", "상품명");
//            parameters.put("quantity", "주문 수량");
//            parameters.put("total_amount", "총 금액");
//            parameters.put("tax_free_amount", "부가세");
//            parameters.put("vat_amount", "상품 비과세 금액");
//            parameters.put("approval_url", "https://developers.kakao.com/success");
//            parameters.put("fail_url", "https://developers.kakao.com/fail");
//            parameters.put("cancel_url", "https://developers.kakao.com/cancel");

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

//            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

            // 외부에 보낼 url
            RestTemplate restTemplate = new RestTemplate();

            kakaoReady = restTemplate.postForObject(
                    "https://open-api.kakaopay.com/online/v1/payment/ready",
                    requestEntity,
                    KakaoReadyResponse.class);
            return kakaoReady;
    }


        /**
         * 결제 완료 승인
         */
        public KakaoApproveResponse approveResponse (String pgToken){

            // 카카오 요청
            Map<String, String> parameters = new HashMap<>();
            parameters.put("cid", payProperties.getCid());
            parameters.put("tid", kakaoReady.getTid());
            parameters.put("partner_order_id", "ORDER_ID");
            parameters.put("partner_user_id", "USER_ID");
            parameters.put("pg_token", pgToken);

            // 파라미터, 헤더
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
            System.out.println();
            System.out.println();
            System.out.println(requestEntity);
            System.out.println();
            System.out.println();

            // 외부에 보낼 url
            RestTemplate restTemplate = new RestTemplate();

            KakaoApproveResponse approveResponse = restTemplate.postForObject(
                    "https://open-api.kakaopay.com/online/v1/payment/approve",
                    requestEntity,
                    KakaoApproveResponse.class);
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println(approveResponse);
            System.out.println();
            System.out.println();
            System.out.println();
            return approveResponse;
    }

    /**
     * 결제 환불
     */
    public KakaoCancelResponse kakaoCancel(String tid) {

        // 카카오페이 요청
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", payProperties.getCid());
        parameters.put("tid", tid);
        parameters.put("cancel_amount", "2200");
        parameters.put("cancel_tax_free_amount", "0");
        parameters.put("cancel_vat_amount", "0");

        // 파라미터, 헤더
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoCancelResponse cancelResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/cancel",
                requestEntity,
                KakaoCancelResponse.class);

        System.out.println();
        System.out.println();
        System.out.println(cancelResponse);
        System.out.println();
        System.out.println();

        return cancelResponse;
    }
}


