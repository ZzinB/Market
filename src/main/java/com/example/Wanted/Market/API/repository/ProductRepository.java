package com.example.Wanted.Market.API.repository;

import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.domain.Product;
import com.example.Wanted.Market.API.domain.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByBuyer(Member buyer);
    List<Product> findBySellerOrBuyerAndStatus(Member seller, Member buyer, ProductStatus status);
}

