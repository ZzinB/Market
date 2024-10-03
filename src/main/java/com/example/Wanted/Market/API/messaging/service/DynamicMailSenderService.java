package com.example.Wanted.Market.API.messaging.service;

import com.example.Wanted.Market.API.messaging.MailConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@RequiredArgsConstructor
public class DynamicMailSenderService {

    private final MailConfigProperties mailConfigProperties;

    public JavaMailSender getJavaMailSender(String provider) {
        MailConfigProperties.MailServerConfig config = switch (provider.toLowerCase()) {
            case "gmail" -> mailConfigProperties.getGmail();
            case "naver" -> mailConfigProperties.getNaver();
            default -> throw new IllegalArgumentException("Unknown mail provider: " + provider);
        };

        Properties props = new Properties();
        props.put("mail.smtp.host", config.getHost());
        props.put("mail.smtp.port", config.getPort());
        props.put("mail.smtp.username", config.getUsername());
        props.put("mail.smtp.password", config.getPassword());
        props.put("mail.smtp.auth", config.getProperties().getSmtp().isAuth());
        props.put("mail.smtp.starttls.enable", config.getProperties().getSmtp().getStarttls().isEnable());
        props.put("mail.smtp.ssl.enable", config.getProperties().getSmtp().isSsl());

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setJavaMailProperties(props);
        return mailSender;
    }
}

