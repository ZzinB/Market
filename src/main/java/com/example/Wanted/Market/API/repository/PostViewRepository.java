package com.example.Wanted.Market.API.repository;

import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.domain.Post;
import com.example.Wanted.Market.API.domain.PostView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostViewRepository extends JpaRepository<PostView, Long> {
    boolean existsByMemberAndPost(Member member, Post post); // 회원과 게시글의 조회 기록 확인

}
