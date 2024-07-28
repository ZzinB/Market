package com.example.Wanted.Market.API.pay;

import com.example.Wanted.Market.API.pay.dto.KakaoApproveResponse;
import com.example.Wanted.Market.API.pay.dto.KakaoCancelResponse;
import com.example.Wanted.Market.API.pay.dto.KakaoReadyResponse;
import com.example.Wanted.Market.API.pay.exception.BusinessLogicException;
import com.example.Wanted.Market.API.pay.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    /**
     * 결제요청
     */
//    @PostMapping("/ready")
//    public ResponseEntity<KakaoReadyResponse> readyToKakaoPay() {
//        KakaoReadyResponse response = kakaoPayService.kakaoPayReady();
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
    @PostMapping("/ready")
    public KakaoReadyResponse readyToKakaoPay() {

        return kakaoPayService.kakaoPayReady();
    }

    /**
     * 결제성공
     */
//    @GetMapping("/success")
//    public ResponseEntity<KakaoApproveResponse> afterPayRequest(@RequestParam("pg_token") String pgToken) {
//        KakaoApproveResponse response = kakaoPayService.ApproveResponse(pgToken);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
    @PostMapping ("/success")
    //public ResponseEntity afterPayRequest(@RequestParam("pg_token") String pgToken) {
    public ResponseEntity<KakaoApproveResponse> afterPayRequest(@RequestParam("pg_token") String pgToken) {

        KakaoApproveResponse kakaoApprove = kakaoPayService.approveResponse(pgToken);

        return new ResponseEntity<>(kakaoApprove, HttpStatus.OK);
    }

    /**
     * 결제 진행 중 취소
     */
//    @GetMapping("/cancel")
//    public ResponseEntity<String> cancel() {
//        return new ResponseEntity<>("Payment canceled", HttpStatus.OK);
//    }
    @GetMapping("/cancel")
    public void cancel() {

        throw new BusinessLogicException(ExceptionCode.PAY_CANCEL);
    }

    /**
     * 결제실패
     */
//    @GetMapping("/fail")
//    public ResponseEntity<String> fail() {
//        return new ResponseEntity<>("Payment failed", HttpStatus.OK);
//    }
    @GetMapping("/fail")
    public void fail() {

        throw new BusinessLogicException(ExceptionCode.PAY_FAILED);
    }

    /**
     * 환불
     */
    @PostMapping("/refund")
    //public ResponseEntity refund() {
    public ResponseEntity<KakaoCancelResponse> refund() {

        KakaoCancelResponse kakaoCancelResponse = kakaoPayService.kakaoCancel();

        return new ResponseEntity<>(kakaoCancelResponse, HttpStatus.OK);
    }
}

