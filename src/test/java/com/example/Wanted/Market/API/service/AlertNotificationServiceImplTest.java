package com.example.Wanted.Market.API.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@SpringBootTest
class AlertNotificationServiceImplTest {

    @InjectMocks
    private AlertNotificationServiceImpl alertNotificationService;

    @Mock
    private DynamicMailSenderService dynamicMailSenderService;

    @Mock
    private JavaMailSender mailSender;

    @Test
    public void testSendNotification() {
        // Given
        String provider = "naver";
        String message = "상품이 판매되었습니다!";
        String recipient = "test@example.com";

        when(dynamicMailSenderService.getJavaMailSender(provider)).thenReturn(mailSender);

        // When
        alertNotificationService.sendNotification(provider, message, recipient);

        // Then
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}
