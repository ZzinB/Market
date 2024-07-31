package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CustomUserDetailsServiceTest {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testLoadUserByUsername() {
        Member member = new Member();
        member.setUsername("testuser");
        member.setPassword(new BCryptPasswordEncoder().encode("password"));
        memberRepository.save(member);

        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertNotNull(userDetails.getPassword());
    }
}

