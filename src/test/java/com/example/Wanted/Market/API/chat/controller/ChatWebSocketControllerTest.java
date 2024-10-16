package com.example.Wanted.Market.API.chat.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.example.Wanted.Market.API.chat.domain.Chat;
import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.domain.Post;
import com.example.Wanted.Market.API.repository.MemberRepository;
import com.example.Wanted.Market.API.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/*
@WebMvcTest(StompController.class)
public class ChatWebSocketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private PostRepository postRepository;

    @InjectMocks
    private StompController chatWebSocketController;

    @BeforeEach
    public void setUp() {
        // Mock SecurityContext
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("testUser"); // 테스트 사용자 이름
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @WithMockUser(username = "testUser") // 인증된 사용자로 테스트
    public void testSendMessage() throws Exception {
        // Given
        Member sender = new Member(); // 적절한 Member 객체 생성
        sender.setUsername("testUser");

        Post post = new Post(); // 적절한 Post 객체 생성
        post.setId(1L);

        Chat chat = new Chat();
        chat.setContent("Hello");
        chat.setReceiver(post.getAuthor()); // 수신자 설정

        when(memberRepository.findByUsername("testUser")).thenReturn(Optional.of(sender));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // When
        Chat result = chatWebSocketController.sendMessage(chat);

        // Then
        assertNotNull(result);
        assertEquals("Hello", result.getContent());
        assertEquals("testUser", result.getSender().getUsername());
    }
}

 */
