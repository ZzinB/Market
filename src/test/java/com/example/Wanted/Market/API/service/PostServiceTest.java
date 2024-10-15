package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.domain.Post;
import com.example.Wanted.Market.API.domain.PostView;
import com.example.Wanted.Market.API.repository.PostRepository;
import com.example.Wanted.Market.API.repository.PostViewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PostServiceTest {
    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostViewRepository postViewRepository;

    private Post post;

    private Member member;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        post = new Post();
        post.setId(1L);
        post.setViewCount(5);

        member = new Member();
        member.setId(1L);
    }

    @Test
    public void 조회수증가_성공(){
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postViewRepository.existsByMemberAndPost(member, post)).thenReturn(false); // 조회 기록 없음
        when(postRepository.save(post)).thenReturn(post); // 저장 후 반환

        // When
        Post updatedPost = postService.incrementViewCount(1L, member);

        // Then
        assertEquals(6, updatedPost.getViewCount());
        verify(postRepository).save(post);
        verify(postViewRepository).save(any(PostView.class)); // 조회 기록 저장 검증
    }

    @Test
    public void 조회수증가_포스트찾을수없음(){
        //Given
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        //When, Then
        assertThrows(EntityNotFoundException.class, () -> {
            postService.incrementViewCount(1L, member);
        });
    }

    @Test
    public void 조회수증가_이미본회원(){
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postViewRepository.existsByMemberAndPost(member, post)).thenReturn(true); // 조회 기록 있음

        // When
        Post updatedPost = postService.incrementViewCount(1L, member);

        // Then
        assertEquals(5, updatedPost.getViewCount()); // 조회수는 증가하지 않음
        verify(postRepository, never()).save(post); // 저장하지 않음
        verify(postViewRepository, never()).save(any(PostView.class)); // 조회 기록도 저장하지 않음
    }
}