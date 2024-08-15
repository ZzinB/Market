package com.example.Wanted.Market.API.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@ToString(exclude = {"orderItems", "categoryItems"})
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Size(max = 200, message = "제목은 200글자 이하로 입력해야 합니다.")
    private String name;

    @Size(max = 1000, message = "내용은 1000글자 이하로 입력해야 합니다.")
    private String description;

    private int price;
    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private Member seller;

    @OneToMany(mappedBy = "item")
    private List<OrderItems> orderItems;

    @OneToMany(mappedBy = "item")
    private List<CategoryItem> categoryItems;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
