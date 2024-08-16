package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Item;
import com.example.Wanted.Market.API.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class AlertNotificationServiceImplTest {

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @Test
    @DisplayName("상품 팔림 알림 - 이메일 전송")
    void notifySale_SendEmailNotification() {
        // Given
        Long itemId = 1L;
        String userEmail = "user@example.com";

        // 테스트에 사용할 아이템 설정
        Item mockItem = new Item();
        mockItem.setItemId(itemId);
        mockItem.setName("Test Item");

        // itemRepository.findById 호출 시 mockItem 반환
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(mockItem));

        // When
        itemService.notifySale(itemId, userEmail);

        // Then
        verify(notificationService, times(1)).sendNotification(anyString(), eq(userEmail));
    }
}