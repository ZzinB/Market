package com.example.Wanted.Market.API.repository;

import com.example.Wanted.Market.API.domain.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems, String> {
}
