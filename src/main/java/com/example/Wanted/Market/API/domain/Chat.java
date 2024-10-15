package com.example.Wanted.Market.API.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender; //메시지 발신자

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver; //메시지 수신자

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 게시글

    @NotBlank(message = "메시지는 필수 입력 값입니다.")
    private String content; //내용
    private LocalDateTime sentAt; //전송 시간

    public Chat(){
        this.sentAt = LocalDateTime.now();
    }
}
