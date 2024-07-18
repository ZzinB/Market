package com.example.Wanted.Market.API.repository;

import com.example.Wanted.Market.API.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
