package com.example.Wanted.Market.API.service;

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

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private MemberRepository memberRepository;

    public Item createItem(Item item) {
        item.setStatus(ProductStatus.AVAILABLE); // 상태를 Enum으로 설정
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
        order.setOrderId(UUID.randomUUID().toString());
        order.setBuyer(buyer);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.ORDERED); // 예약 상태로 설정

        // 주문 상품 생성
        OrderItems orderItem = new OrderItems();
        orderItem.setOrderItemId(UUID.randomUUID().toString());
        orderItem.setItem(item);
        orderItem.setOrder(order);
        orderItem.setOrderPrice(item.getPrice());
        orderItem.setCount(1);

        List<OrderItems> orderItemsList = new ArrayList<>();
        orderItemsList.add(orderItem);
        order.setOrderItems(orderItemsList);

        // 주문 저장 및 반환
        return ordersRepository.save(order);
    }

    public Item getItemById(String itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

}
