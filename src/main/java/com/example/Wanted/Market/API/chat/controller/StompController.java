package com.example.Wanted.Market.API.chat.controller;

import com.example.Wanted.Market.API.chat.domain.Chat;
import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.domain.Post;
import com.example.Wanted.Market.API.repository.MemberRepository;
import com.example.Wanted.Market.API.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * 클라이언트로부터 전달받은 메시지 처리
 */
@Controller
@RequiredArgsConstructor
public class StompController {

//    @Autowired
//    private MemberRepository memberRepository; // 사용자 정보 접근을 위한 리포지토리
//    @Autowired
//    private PostRepository postRepository; // 게시글 정보 접근을 위한 리포지토리

    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * @MessageMapping : 메시지 경로 매핑
     * @SendTo / SimpMessagingTemplate : 메시지를 클라이언트에게 전달
     */
    @MessageMapping("/chat/send")
    public void sendMessage(@Payload Map<String, Object> data) {
//        // 현재 인증된 사용자 정보 가져오기
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        Member sender = memberRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        // 발신자 설정
//        chat.setSender(sender);
//
//        // 게시글 ID를 통해 수신자 설정
//        Post post = postRepository.findById(chat.getPostId())
//                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
//        chat.setReceiver(post.getAuthor()); // 게시글 작성자에게 보냄
//
//        // 추가적인 로직: DB 저장 등
//        return chat;
        simpMessagingTemplate.convertAndSend("/topic/1",data);

    }
}

