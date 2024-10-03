package com.example.Wanted.Market.API.messaging.service;

import com.example.Wanted.Market.API.domain.Item;

public interface NotificationService {
    void sendWarning(Item item);
    void sendAlert(Item item);

    void sendNotification(String provider, String message, String recipient); // 메일
    boolean sendSocialNotification(String provider, String message, String recipient); // 소셜 알림
}
