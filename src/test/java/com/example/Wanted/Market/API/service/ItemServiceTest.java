package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.Payment.PaymentService;
import com.example.Wanted.Market.API.Payment.dto.PaymentRequest;
import com.example.Wanted.Market.API.Payment.dto.PaymentResponse;
import com.example.Wanted.Market.API.Payment.dto.PaymentStatus;
import com.example.Wanted.Market.API.domain.*;
import com.example.Wanted.Market.API.repository.ItemRepository;
import com.example.Wanted.Market.API.repository.MemberRepository;
import com.example.Wanted.Market.API.repository.OrdersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class ItemServiceTest {
    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private OrdersRepository ordersRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private ItemService itemService;

    @Test
    @DisplayName("상품 등록 - 제목 및 내용 길이 제한")
    void createItem_TitleAndDescriptionLength() {
        // Given
        Item item = new Item();
        item.setName("Valid Item");
        item.setPrice(100);
        item.setStockQuantity(10);

        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item savedItem = invocation.getArgument(0);
            savedItem.setItemId(1L); // Simulate an ID being set by the database
            savedItem.setCreatedAt(LocalDateTime.now());
            savedItem.setUpdatedAt(LocalDateTime.now());
            return savedItem;
        });

        // When
        Item savedItem = itemService.createItem(item);

        // Debugging
        System.out.println("Saved Item: " + savedItem);

        // Then
        assertNotNull(savedItem, "Saved item should not be null.");
        assertNotNull(savedItem.getCreatedAt(), "Created date should be set automatically.");
        assertNotNull(savedItem.getUpdatedAt(), "Last modified date should be set automatically.");
        assertTrue(savedItem.getCreatedAt().isBefore(savedItem.getUpdatedAt()), "Updated date should be after created date.");
    }

    @Test
    @DisplayName("상품 등록 - 기본 재고 수량 설정")
    void createItem_StockQuantity() {
        // Given
        Item item = new Item();
        item.setName("Valid Item");
        item.setPrice(100);
        item.setStockQuantity(10);

        // When
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Item savedItem = itemService.createItem(item);

        // Then
        assertNotNull(savedItem);
        assertEquals(1, savedItem.getStockQuantity(), "Stock quantity should be set to 1 by default.");
    }

    @Test
    @DisplayName("상품 구매 - 결제 금액 필수")
    void purchaseItem_PaymentAmountRequired() {
        // Given
        Long itemId = 1L;
        String buyerEmail = "buyer@example.com";

        Item item = new Item();
        item.setItemId(itemId);
        item.setPrice(100);
        item.setStatus(ProductStatus.AVAILABLE);

        Member seller = new Member();
        seller.setId(1L);
        item.setSeller(seller);

        Member buyer = new Member();
        buyer.setEmail(buyerEmail);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(memberRepository.findByEmail(buyerEmail)).thenReturn(Optional.of(buyer));
        when(ordersRepository.save(any(Orders.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setTransactionId("12345");

        when(paymentService.processPayment(any(PaymentRequest.class))).thenReturn(paymentResponse);

        PaymentStatus paymentStatus = new PaymentStatus();
        paymentStatus.setTransactionId("12345");
        paymentStatus.setStatus("SUCCESS");
        paymentStatus.setAmount(100L);
        paymentStatus.setMessage("Payment successful");

        when(paymentService.checkPaymentStatus("12345")).thenReturn(paymentStatus);

        // When
        Orders order = itemService.purchaseItem(itemId, buyerEmail);

        // Then
        assertNotNull(order);
        assertEquals(OrderStatus.ORDERED, order.getStatus(), "Order status should be ORDERED.");
    }

    @Test
    @DisplayName("상품 구매 - 결제 실패 처리")
    void purchaseItem_PaymentFailure() {
        // Given
        Long itemId = 1L;
        String buyerEmail = "buyer@example.com";
        Item item = new Item();
        item.setItemId(itemId);
        item.setPrice(100);
        item.setStatus(ProductStatus.AVAILABLE);

        Member seller = new Member();
        seller.setId(1L);
        item.setSeller(seller);


        Member buyer = new Member();
        buyer.setEmail(buyerEmail);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(memberRepository.findByEmail(buyerEmail)).thenReturn(Optional.of(buyer));
        when(ordersRepository.save(any(Orders.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setTransactionId("12345");

        when(paymentService.processPayment(any(PaymentRequest.class))).thenReturn(paymentResponse);

        PaymentStatus paymentStatus = new PaymentStatus();
        paymentStatus.setTransactionId("12345");
        paymentStatus.setStatus("FAILURE");
        paymentStatus.setAmount(100L);
        paymentStatus.setMessage("Payment failed");

        when(paymentService.checkPaymentStatus("12345")).thenReturn(paymentStatus);

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            itemService.purchaseItem(itemId, buyerEmail);
        });

        assertEquals("Payment failed", thrown.getMessage(), "Exception message should be 'Payment failed'.");
    }

    @Test
    @DisplayName("상품 등록 - 생성 및 수정 시간 자동 관리")
    void createItem_Timestamps() {
        // Given
        Item item = new Item();
        item.setName("Valid Item");
        item.setPrice(100);
        item.setStockQuantity(10);

        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item savedItem = invocation.getArgument(0);
            savedItem.setCreatedAt(LocalDateTime.now().minusDays(1));
            savedItem.setUpdatedAt(LocalDateTime.now());
            return savedItem;
        });

        // When
        Item savedItem = itemRepository.save(item);

        // Then
        assertNotNull(savedItem, "Saved item should not be null.");
        assertNotNull(savedItem.getCreatedAt(), "Created date should be set automatically.");
        assertNotNull(savedItem.getUpdatedAt(), "Last modified date should be set automatically.");
        assertTrue(savedItem.getCreatedAt().isBefore(savedItem.getUpdatedAt()), "Updated date should be after created date.");
    }


    @Test
    @DisplayName("상품 수정 - 생성일 9일째 수정 불가 경고 알림")
    void updateItem_9DaysAlertNotification() {
        // Given
        Item existingItem = new Item();
        existingItem.setItemId(1L);
        existingItem.setName("Original Item");
        existingItem.setPrice(100);
        existingItem.setStockQuantity(10);
        existingItem.setCreatedAt(LocalDateTime.now().minusDays(9));
        existingItem.setUpdatedAt(LocalDateTime.now());

        Item newItemData = new Item();
        newItemData.setName("Updated Item");
        newItemData.setPrice(200);
        newItemData.setStockQuantity(20);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(existingItem));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        itemService.updateItem(1L, newItemData);

        // Then
        verify(notificationService).sendWarning(existingItem);
        verify(notificationService, never()).sendAlert(any(Item.class));
    }


    @Test
    @DisplayName("상품 수정 - 생성일 10일 이후 수정 시 수정 불가 알림 전송")
    void updateItem_10DaysAfterModificationNotAllowed() {
        // Given
        LocalDateTime creationDate = LocalDateTime.now().minusDays(10); // 10일 전
        Item existingItem = new Item();
        existingItem.setItemId(1L);
        existingItem.setCreatedAt(creationDate);
        existingItem.setUpdatedAt(creationDate);

        Item newItemData = new Item();
        newItemData.setName("Updated Item");
        newItemData.setPrice(200);
        newItemData.setStockQuantity(20);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(existingItem));

        // When & Then
        assertThrows(IllegalStateException.class, () -> {
            itemService.updateItem(1L, newItemData);
        });

        // Verify that the alert for modification being not allowed was sent
        verify(notificationService).sendAlert(any(Item.class));
    }
}