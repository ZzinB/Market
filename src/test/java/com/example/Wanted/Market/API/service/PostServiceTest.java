package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Post;
import com.example.Wanted.Market.API.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostServiceTest {
    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    private Post post;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        post = new Post();
        post.setId(1L);
        post.setViewCount(5);
    }

    @Test
    public void 조회수증가_성공(){
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);  // save 메서드도 모킹

        // When
        Post updatedPost = postService.incrementViewCount(1L);

        // Then
        assertNotNull(updatedPost);
        assertEquals(6, updatedPost.getViewCount());
        verify(postRepository).save(post);
    }

    @Test
    public void 조회수증가_포스트찾을수없음(){
        //Given
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        //When, Then
        assertThrows(EntityNotFoundException.class, () -> {
            postService.incrementViewCount(1L);
        });
    }
}