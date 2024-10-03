package com.example.Wanted.Market.API.messaging.service;

import com.example.Wanted.Market.API.domain.Item;
import com.example.Wanted.Market.API.domain.SocialType;
import com.example.Wanted.Market.API.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class AlertNotificationServiceImpl implements NotificationService {
    private final DynamicMailSenderService dynamicMailSenderService;
    private final CustomMessageService customMessageService;
    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @Override
    public void sendWarning(Item item) {
        log.warn("Warning: Item {} 알림: 상품 등록 10일이 지나 수정이 불가능합니다.", item.getItemId());
    }

    @Override
    public void sendAlert(Item item) {
        // 생성일 9일째 경고 알림 구현
        log.info("Alert: Item {} 알림: 상품 등록 9일 째 되는 날 입니다. 추후 수정은 불가능합니다.", item.getItemId());
    }

    @Override
    public void sendNotification(String provider, String message, String recipient) {
        JavaMailSender mailSender = dynamicMailSenderService.getJavaMailSender(provider);

        // 메일 전송
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipient);
        mailMessage.setSubject("상품 판매 알림");
        mailMessage.setText(message);

        mailSender.send(mailMessage);
        System.out.println("Email sent using " + provider + " to: " + recipient);
    }

    @Override
    public boolean sendSocialNotification(String provider, String message, String recipient) {
        switch (provider.toLowerCase()) {
            case "kakao":
                boolean response = customMessageService.sendMyMessage();
                if (response) {
                    log.info("Kakao message sent successfully.");
                } else {
                    log.error("Failed to send Kakao message.");
                }
                return response;
            case "naver":
                sendNotification("naver", message, recipient);
                return true;
            default:
                throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
    }
}
