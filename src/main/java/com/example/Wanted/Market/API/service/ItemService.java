package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.Payment.PaymentService;
import com.example.Wanted.Market.API.Payment.dto.PaymentRequest;
import com.example.Wanted.Market.API.Payment.dto.PaymentResponse;
import com.example.Wanted.Market.API.Payment.dto.PaymentStatus;
import com.example.Wanted.Market.API.domain.*;
import com.example.Wanted.Market.API.dto.ItemDetailResponse;
import com.example.Wanted.Market.API.exception.ResourceNotFoundException;
import com.example.Wanted.Market.API.messaging.service.NotificationService;
import com.example.Wanted.Market.API.repository.ItemRepository;
import com.example.Wanted.Market.API.repository.MemberRepository;
import com.example.Wanted.Market.API.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 물건 관리 및 구매 처리
 */
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final OrdersRepository ordersRepository;
    private final MemberRepository memberRepository;
    private final PaymentService paymentService;

    private final NotificationService notificationService;

    @Autowired
    public ItemService(ItemRepository itemRepository, OrdersRepository ordersRepository,
                       MemberRepository memberRepository, PaymentService paymentService,
                       NotificationService notificationService) {
        this.itemRepository = itemRepository;
        this.ordersRepository = ordersRepository;
        this.memberRepository = memberRepository;
        this.paymentService = paymentService;
        this.notificationService = notificationService;
    }

    public Item createItem(Item item) {
        item.setStatus(ProductStatus.AVAILABLE); // 상태를 Enum(AVAILABLE)으로 설정
        item.setStockQuantity(1); // 기본 재고 수량 설정
        return itemRepository.save(item);
    }

    public Orders purchaseItem(Long itemId, String buyerEmail) {
        // 상품 조회 및 예약 상태 변경
        Item item = getItemById(itemId);
        if (item.getStatus() != ProductStatus.AVAILABLE) {
            throw new IllegalStateException("Item is not available for purchase");
        }
        item.setStatus(ProductStatus.RESERVED);
        itemRepository.save(item);

        // 구매자 조회
        Member buyer = memberRepository.findByEmail(buyerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found"));


        Member seller = item.getSeller();
        if (seller == null) {
            throw new IllegalStateException("Seller cannot be null");
        }

        // 주문 생성
        Orders order = new Orders();
        order.setBuyer(buyer);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.ORDERED); // 주문이 들어온 물건을 예약 상태로 설정

        // 주문 상품 생성
        OrderItems orderItem = new OrderItems();
        orderItem.setItem(item);
        orderItem.setOrder(order);
        orderItem.setOrderPrice(item.getPrice());
        orderItem.setCount(1);

        List<OrderItems> orderItemsList = new ArrayList<>();
        orderItemsList.add(orderItem);
        order.setOrderItems(orderItemsList);

        Orders savedOrder = ordersRepository.save(order);

        // 결제 처리
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount((long) item.getPrice()); // 금액을 long으로 변환
        PaymentResponse paymentResponse = paymentService.processPayment(paymentRequest);

        PaymentStatus paymentStatus = paymentService.checkPaymentStatus(paymentResponse.getTransactionId());
        if ("SUCCESS".equals(paymentStatus.getStatus())) {
            item.setStatus(ProductStatus.COMPLETED); // 결제 완료 시 상태를 COMPLETED로 변경
            itemRepository.save(item);

            // 판매자에게 포인트 전송
            double sellerEarnings = item.getPrice() * 0.9; // 90% 지급
            seller.setPoints(seller.getPoints() + sellerEarnings);
            memberRepository.save(seller);

            // 판매자에게 알림 전송
            notifyUserOnSale(seller, item.getName());

        } else {
            throw new RuntimeException("Payment failed");
        }

        return savedOrder;
    }

    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    /**
     * 상품의 수정 가능일과 현재 날짜 기준 수정 가능 여부를 반환합니다.
     * @param itemId Item ID
     * @return 수정 가능일 및 수정 가능 여부
     */
    public ItemDetailResponse getItemDetails(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        LocalDate modificationAllowedDate = calDate(item);
        boolean canBeModified = modificationAllowedDate != null && LocalDate.now().isBefore(modificationAllowedDate);

        return new ItemDetailResponse(item, modificationAllowedDate, canBeModified);
    }

    /**
     * 상품의 수정 가능일을 계산합니다.
     * @param item Item 객체
     * @return 수정 가능일 (10일이 되기 하루 전), 수정 불가 시 null
     */
    private LocalDate calDate(Item item) {
        LocalDateTime now = LocalDateTime.now();
        long daysBetween = ChronoUnit.DAYS.between(item.getCreatedAt(), now);

        if (daysBetween >= 10) {
            return null; // 10일 이후에는 수정 불가
        }

        // 수정 가능일 계산 (10일이 되는 날)
        LocalDateTime modificationAllowedDate = item.getCreatedAt().plusDays(10);
        return modificationAllowedDate.toLocalDate();
    }

    public Item updateItem(Long itemId, Item newItemData) {
        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        LocalDate modificationAllowedDate = calDate(existingItem);
        LocalDate now = LocalDate.now();

        if (modificationAllowedDate == null) {
            notificationService.sendAlert(existingItem);
            throw new IllegalStateException("Item cannot be updated after 10 days from creation.");
        }

        // Update item details
        existingItem.setName(newItemData.getName());
        existingItem.setPrice(newItemData.getPrice());
        existingItem.setStockQuantity(newItemData.getStockQuantity());
        existingItem.setUpdatedAt(LocalDateTime.now());

        itemRepository.save(existingItem);

        // Check for 9-day alert
        if (modificationAllowedDate.minusDays(1).equals(now)) {
            notificationService.sendWarning(existingItem);
        }
        return existingItem;
    }

    /**
     * 상품 목록 조회
     * @param title 검색할 제목 (부분 검색 가능)
     * @param sortOrder 정렬 기준 (오름차순 또는 내림차순)
     * @return 상품 목록
     */
    public List<Item> getItems(String title, String sortOrder) {
        if (title == null || title.trim().isEmpty()) {
            // 제목이 없을 경우 생성일 기준으로 정렬 (오름차순 또는 내림차순)
            if ("ASC".equalsIgnoreCase(sortOrder)) {
                return itemRepository.findAllByDeletedAtIsNullOrderByCreatedAtAsc();
            } else {
                return itemRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc();
            }
        } else {
            // 제목 기준 부분 검색 및 정렬
            if ("ASC".equalsIgnoreCase(sortOrder)) {
                return itemRepository.findByNameContainingIgnoreCaseAndDeletedAtIsNullOrderByCreatedAtAsc(title);
            } else {
                return itemRepository.findByNameContainingIgnoreCaseAndDeletedAtIsNullOrderByCreatedAtDesc(title);
            }
        }
    }

    public void softDeleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        item.setDeletedAt(LocalDateTime.now());
        itemRepository.save(item);
    }

    public void hardDeleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        itemRepository.delete(item);
    }

    public void notifyUserOnSale(Member member, String itemName) {
        String message = "축하합니다! 귀하의 물건이 판매되었습니다: " + itemName;
        if (member.isSocial()) { // 소셜 로그인 사용자일 경우
            String provider = member.getSocialType().toString();; // 소셜 로그인 타입 가져오기

            if ("KAKAO".equals(provider)) {
                // 액세스 토큰을 직접 사용하여 카카오톡 알림 전송
                String accessToken = member.getAccessToken(); // Member 클래스에서 액세스 토큰 가져오기
                boolean success = notificationService.sendSocialNotification("kakao", message, accessToken);

                if (success) {
                    System.out.println("카카오톡 알림이 성공적으로 전송되었습니다.");
                } else {
                    System.out.println("카카오톡 알림 전송에 실패했습니다.");
                }
            } else if ("NAVER".equals(provider)) {
                String email = member.getEmail(); // 네이버 이메일로 알림 전송
                notificationService.sendNotification("naver", message, email);
                System.out.println("네이버 메일로 알림이 성공적으로 전송되었습니다.");
            }
        } else { // 일반 로그인 사용자일 경우
            String email = member.getEmail();
            notificationService.sendNotification("email", message, email);
            System.out.println("이메일 알림이 성공적으로 전송되었습니다.");
        }
    }
}
