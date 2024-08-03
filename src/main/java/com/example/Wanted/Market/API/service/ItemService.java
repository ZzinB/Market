package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.Payment.PaymentService;
import com.example.Wanted.Market.API.Payment.dto.PaymentRequest;
import com.example.Wanted.Market.API.Payment.dto.PaymentResponse;
import com.example.Wanted.Market.API.Payment.dto.PaymentStatus;
import com.example.Wanted.Market.API.domain.*;
import com.example.Wanted.Market.API.exception.ResourceNotFoundException;
import com.example.Wanted.Market.API.repository.ItemRepository;
import com.example.Wanted.Market.API.repository.MemberRepository;
import com.example.Wanted.Market.API.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 물건 관리 및 구매 처리
 */
@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PaymentService paymentService;

    public ItemService(ItemRepository itemRepository, MemberRepository memberRepository) {
        this.itemRepository = itemRepository;
        this.memberRepository = memberRepository;
    }

    public Item createItem(Item item) {
        item.setStatus(ProductStatus.AVAILABLE); // 상태를 Enum(AVAILABLE)으로 설정
        item.setStockQuantity(1);
        return itemRepository.save(item);
    }

    public Orders purchaseItem(String itemId, String buyerEmail) {
        // 상품 조회 및 예약 상태 변경
        Item item = getItemById(itemId);
        if (item.getStatus() != ProductStatus.AVAILABLE) {
            throw new IllegalStateException("Item is not available for purchase");
        }
        item.setStatus(ProductStatus.RESERVED);
        itemRepository.save(item);

        // 구매자 조회
        Member buyer = memberRepository.findByEmail(buyerEmail);
        if (buyer == null) {
            throw new ResourceNotFoundException("Buyer not found");
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
        //paymentRequest.setDescription("Purchase of item: " + item.getId());
        PaymentResponse paymentResponse = paymentService.processPayment(paymentRequest);

        PaymentStatus paymentStatus = paymentService.checkPaymentStatus(paymentResponse.getTransactionId());
        if ("SUCCESS".equals(paymentStatus.getStatus())) {
            item.setStatus(ProductStatus.COMPLETED); // 결제 완료 시 상태를 COMPLETED로 변경
            itemRepository.save(item);
        } else {
            throw new RuntimeException("Payment failed");
        }

        // 판매자에게 포인트 전송
        double sellerEarnings = item.getPrice() * 0.9; // 90% 지급
        Member seller = item.getSeller();
        seller.setPoints(seller.getPoints() + sellerEarnings);
        memberRepository.save(seller);

        // 물건 상태 업데이트
        item.setStatus(ProductStatus.COMPLETED);
        itemRepository.save(item);

        return savedOrder;
    }

    public Item getItemById(String itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

}
