package com.example.Wanted.Market.API.controller;

import com.example.Wanted.Market.API.domain.Post;
import com.example.Wanted.Market.API.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id){
        Post post = postService.incrementViewCount(id);
        return ResponseEntity.ok(post);
    }
}