package com.example.Wanted.Market.API.repository;

import com.example.Wanted.Market.API.domain.Orders;
import com.example.Wanted.Market.API.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
