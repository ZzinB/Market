package com.example.Wanted.Market.API.repository;

import com.example.Wanted.Market.API.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // 제목 기준 부분 검색 및 삭제되지 않은 상품 조회 (오름차순)
    List<Item> findByNameContainingIgnoreCaseAndDeletedAtIsNullOrderByCreatedAtAsc(String title);

    // 제목 기준 부분 검색 및 삭제되지 않은 상품 조회 (내림차순)
    List<Item> findByNameContainingIgnoreCaseAndDeletedAtIsNullOrderByCreatedAtDesc(String title);

    // 삭제되지 않은 상품을 생성일 기준으로 오름차순 정렬하여 조회
    List<Item> findAllByDeletedAtIsNullOrderByCreatedAtAsc();

    // 삭제되지 않은 상품을 생성일 기준으로 내림차순 정렬하여 조회
    List<Item> findAllByDeletedAtIsNullOrderByCreatedAtDesc();

}