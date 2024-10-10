package com.example.Wanted.Market.API.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "post_views")
public class PostView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 조회한 회원

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 조회한 게시글

    @Column(name = "view_timestamp", nullable = false)
    private LocalDateTime viewTimestamp;

    public PostView() {
        this.viewTimestamp = LocalDateTime.now();
    }
}
