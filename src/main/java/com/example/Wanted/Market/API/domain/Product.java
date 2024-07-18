package com.example.Wanted.Market.API.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double price;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "memberId")
    private Member seller;

    @ManyToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "memberId")
    private Member buyer;
}
