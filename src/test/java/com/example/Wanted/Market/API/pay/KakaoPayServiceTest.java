package com.example.Wanted.Market.API.pay;

import com.example.Wanted.Market.API.pay.kakaopay.KakaoPayService;
import com.example.Wanted.Market.API.pay.kakaopay.dto.KakaoApproveResponse;
import com.example.Wanted.Market.API.pay.kakaopay.dto.KakaoReadyResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class KakaoPayServiceTest {

    @Autowired
    private KakaoPayService kakaoPayService;

    @Test
    public void testKakaoPayReady() {
        KakaoReadyResponse response = kakaoPayService.kakaoPayReady();
        assertThat(response).isNotNull();
        assertThat(response.getTid()).isNotEmpty();
        assertThat(response.getNext_redirect_pc_url()).isNotEmpty();
    }

    @Test
    public void testApproveResponse() {
        KakaoReadyResponse readyResponse = kakaoPayService.kakaoPayReady();
        KakaoApproveResponse approveResponse = kakaoPayService.approveResponse("pgToken");
        assertThat(approveResponse).isNotNull();
        assertThat(approveResponse.getTid()).isEqualTo(readyResponse.getTid());
    }
}