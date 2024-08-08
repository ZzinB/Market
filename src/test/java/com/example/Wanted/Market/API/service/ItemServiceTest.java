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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private PaymentService paymentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateItem() {
        Item item = new Item();
        item.setName("Test Item");
        item.setPrice(100);
        item.setStockQuantity(10);
        item.setStatus(ProductStatus.AVAILABLE);

        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item createdItem = itemService.createItem(item);

        assertNotNull(createdItem);
        assertEquals("Test Item", createdItem.getName());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void testPurchaseItem() {
        //Given
        Item item = new Item();
        item.setItemId(1L);
        item.setName("Test Item");
        item.setPrice(100);
        item.setStockQuantity(10);
        item.setStatus(ProductStatus.AVAILABLE);

        Member seller = new Member();
        seller.setId(2L);
        seller.setEmail("seller@example.com");

        item.setSeller(seller);

        Member buyer = new Member();
        buyer.setId(1L);
        buyer.setEmail("buyer@example.com");
        buyer.setPoints(100);

        Orders order = new Orders();
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.ORDERED);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(memberRepository.findByEmail("buyer@example.com")).thenReturn(buyer);
        when(ordersRepository.save(any(Orders.class))).thenReturn(order);

        // Mock PaymentStatus
        PaymentStatus paymentStatus = new PaymentStatus();
        paymentStatus.setTransactionId("txn123");
        paymentStatus.setStatus("SUCCESS");
        paymentStatus.setAmount(100L);
        paymentStatus.setMessage("Payment successful");

        // Mock PaymentResponse
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setTransactionId("txn123");

        when(paymentService.processPayment(any(PaymentRequest.class))).thenReturn(paymentResponse);
        when(paymentService.checkPaymentStatus(anyString())).thenReturn(paymentStatus);

        Orders savedOrder = itemService.purchaseItem(1L, "buyer@example.com");

        // Verify results
        assertNotNull(savedOrder);
        assertEquals(OrderStatus.ORDERED, savedOrder.getStatus());
        assertEquals(ProductStatus.COMPLETED, item.getStatus());
        verify(itemRepository, times(2)).save(any(Item.class));
        verify(ordersRepository, times(1)).save(any(Orders.class));
        verify(paymentService, times(1)).processPayment(any(PaymentRequest.class));
        verify(paymentService, times(1)).checkPaymentStatus("txn123");
    }

    @Test
    void testGetItemById() {
        Item item = new Item();
        item.setItemId(1L);
        item.setName("Test Item");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Item foundItem = itemService.getItemById(1L);

        assertNotNull(foundItem);
        assertEquals("Test Item", foundItem.getName());
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllItems() {
        Item item1 = new Item();
        item1.setName("Test Item 1");

        Item item2 = new Item();
        item2.setName("Test Item 2");

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        when(itemRepository.findAll()).thenReturn(items);

        List<Item> itemList = itemService.getAllItems();

        assertNotNull(itemList);
        assertEquals(2, itemList.size());
        verify(itemRepository, times(1)).findAll();
    }
}
