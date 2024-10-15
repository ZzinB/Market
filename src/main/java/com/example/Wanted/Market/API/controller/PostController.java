package com.example.Wanted.Market.API.controller;

import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.domain.Post;
import com.example.Wanted.Market.API.repository.MemberRepository;
import com.example.Wanted.Market.API.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private MemberRepository memberRepository;
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id, @RequestParam Long userId){
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        Post post = postService.incrementViewCount(id, member);
        return ResponseEntity.ok(post);
    }
}