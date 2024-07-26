package com.example.Wanted.Market.API.pay;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PayPropertiesTest {

    @Autowired
    private KakaoPayProperties payProperties;

    @Test
    public void whenApplicationStarts_thenPayPropertiesLoaded() {
        assertThat(payProperties.getSecretKey()).isNotNull();
        assertThat(payProperties.getCid()).isEqualTo("TC0ONETIME");
        // SECRET_KEY-key 값 출력
        System.out.println("SECRET_KEY: " + payProperties.getSecretKey());
    }
}
