package com.example.Wanted.Market.API.repository;

import com.example.Wanted.Market.API.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, String> {
}
