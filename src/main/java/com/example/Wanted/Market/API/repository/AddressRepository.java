package com.example.Wanted.Market.API.repository;

import com.example.Wanted.Market.API.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
