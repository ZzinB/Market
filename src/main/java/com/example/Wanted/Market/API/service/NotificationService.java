package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Item;

public interface NotificationService {
    void sendWarning(Item item);
    void sendAlert(Item item);

    void sendNotification(String message, String recipient);
}
