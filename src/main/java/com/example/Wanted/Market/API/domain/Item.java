package com.example.Wanted.Market.API.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String itemId;

    private String name;
    private int price;
    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "memberId")
    private Member seller;

    @OneToMany(mappedBy = "item")
    private List<OrderItems> orderItems;

    @OneToMany(mappedBy = "item")
    private List<CategoryItem> categoryItems;
}
