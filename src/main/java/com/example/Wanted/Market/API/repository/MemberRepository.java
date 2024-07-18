package com.example.Wanted.Market.API.repository;

import com.example.Wanted.Market.API.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    Member findByEmail(String email);

}

