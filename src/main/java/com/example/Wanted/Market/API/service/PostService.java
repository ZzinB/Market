package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Post;
import com.example.Wanted.Market.API.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post incrementViewCount(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("글을 못 찾아요!"));

        post.setViewCount(post.getViewCount() + 1);  //조회수 증가
        return postRepository.save(post);  //저장
    }
}
