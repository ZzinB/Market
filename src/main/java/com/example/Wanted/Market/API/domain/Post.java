package com.example.Wanted.Market.API.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member author;  //게시글 작성자

    private String title;
    private String content;

    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = true)
    private Item item; // 연결된 상품

    public Post() {
        this.createdDate = LocalDateTime.now();
    }
}
