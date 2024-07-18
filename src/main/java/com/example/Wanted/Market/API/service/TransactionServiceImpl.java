package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.config.TransactionNotFoundException;
import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.domain.Product;
import com.example.Wanted.Market.API.domain.ProductStatus;
import com.example.Wanted.Market.API.domain.Transaction;
import com.example.Wanted.Market.API.repository.MemberRepository;
import com.example.Wanted.Market.API.repository.ProductRepository;
import com.example.Wanted.Market.API.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    @Transactional
    public void approveTransaction(Long transactionId, String sellerUsername) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
        Member seller = memberRepository.findByUsername(sellerUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!transaction.getSeller().getMemberId().equals(seller.getMemberId())) {
            throw new AccessDeniedException("You are not authorized to approve this transaction");
        }

        transaction.setStatus("완료됨");
        Product product = transaction.getProduct();
        product.setStatus(ProductStatus.COMPLETED); // Enum 값으로 설정
        productRepository.save(product);
        transactionRepository.save(transaction);
    }

    @Override
    public List<Product> getPurchases(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return productRepository.findByBuyer(member);
    }

    @Override
    public List<Product> getReservations(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return productRepository.findBySellerOrBuyerAndStatus(member, member, ProductStatus.RESERVED); // Enum 값으로 설정
    }
}

