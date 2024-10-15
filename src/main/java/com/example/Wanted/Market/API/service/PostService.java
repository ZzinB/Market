package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.domain.Post;
import com.example.Wanted.Market.API.domain.PostView;
import com.example.Wanted.Market.API.repository.PostRepository;
import com.example.Wanted.Market.API.repository.PostViewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostViewRepository postViewRepository;

    public Post incrementViewCount(Long postId, Member member){
        //사용자가 조회한적 있는지 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("글을 못 찾아요!"));

        if (postViewRepository.existsByMemberAndPost(member, post)) {
            return post; // 조회수 증가 없이 포스트 반환
        }

        //조회 수 증가
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);

        //조회 기록 저장
        PostView postView = new PostView();
        postView.setMember(member);
        postView.setPost(post);
        postViewRepository.save(postView);
        return post;
    }
}
