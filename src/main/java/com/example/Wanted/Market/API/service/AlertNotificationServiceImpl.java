package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Item;
import org.springframework.stereotype.Service;

@Service
public class AlertNotificationServiceImpl implements NotificationService {

    @Override
    public void sendWarning(Item item) {
        System.out.println("Warning: Item " + item.getItemId() + " 알림: 상품 등록 10일이 지나 수정이 불가능합니다. ");
    }

    @Override
    public void sendAlert(Item item) {
        // 생성일 9일째 경고 알림 구현
        System.out.println("Alert: Item " + item.getItemId() + " 알림: 상품 등록 9일 째 되는 날 입니다. 추후 수정은 불가능합니다.");
    }
}

