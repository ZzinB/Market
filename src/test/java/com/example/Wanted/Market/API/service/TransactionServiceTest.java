package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.domain.Product;
import com.example.Wanted.Market.API.domain.Transaction;
import com.example.Wanted.Market.API.repository.MemberRepository;
import com.example.Wanted.Market.API.repository.ProductRepository;
import com.example.Wanted.Market.API.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransaction() {
        // Product 설정
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        // Seller 설정
        Member seller = new Member();
        seller.setId(1L);
        seller.setEmail("seller@example.com");

        // Buyer 설정
        Member buyer = new Member();
        buyer.setId(2L);
        buyer.setEmail("buyer@example.com");

        // Transaction 설정
        Transaction transaction = new Transaction();
        transaction.setProduct(product);
        transaction.setSeller(seller);
        transaction.setBuyer(buyer);
        transaction.setStatus("COMPLETED");

        // Mock 설정
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(memberRepository.findById(2L)).thenReturn(Optional.of(buyer));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // TransactionService의 메서드 호출
        Transaction savedTransaction = transactionService.createTransaction(product.getId(), seller.getId(), buyer.getId(), "COMPLETED");

        // 결과 검증
        assertNotNull(savedTransaction);
        assertEquals("COMPLETED", savedTransaction.getStatus());
        assertEquals(product.getId(), savedTransaction.getProduct().getId());
        assertEquals(seller.getId(), savedTransaction.getSeller().getId());
        assertEquals(buyer.getId(), savedTransaction.getBuyer().getId());
        verify(productRepository, times(1)).findById(product.getId());
        verify(memberRepository, times(1)).findById(seller.getId());
        verify(memberRepository, times(1)).findById(buyer.getId());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
}
