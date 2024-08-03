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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemServiceTest {

    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member existingSeller;


    @BeforeEach
    public void setUp() {
        itemService = new ItemService(itemRepository, memberRepository);

        // 데이터베이스에 이미 등록된 회원
        existingSeller = memberRepository.findByEmail("testuser@example.com");

        // 테스트에서 사용할 회원이 없는 경우 예외 처리
        if (existingSeller == null) {
            throw new RuntimeException("Test setup failed: existing user not found.");
        }
    }

    @Test
    public void itemPurchaseWithPoints() throws Exception {
        // 1. 물건 등록
        Item item = new Item();
        item.setName("Test Item");
        item.setPrice(500);
        item.setStatus(ProductStatus.AVAILABLE);
        item.setSeller(existingSeller);

        Item savedItem = itemService.createItem(item);

        // 2. 검증
        assertNotNull(savedItem.getItemId(), "Item ID should be generated");
        assertEquals("Test Item", savedItem.getName(), "Item name should match");
        assertEquals(existingSeller, savedItem.getSeller(), "Seller should match");
        assertEquals(ProductStatus.AVAILABLE, savedItem.getStatus(), "Item status should be AVAILABLE");
    }
}