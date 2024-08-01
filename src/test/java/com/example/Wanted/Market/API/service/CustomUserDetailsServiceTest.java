package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Address;
import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.domain.Role;
import com.example.Wanted.Market.API.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomUserDetailsServiceTest {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testLoadUserByUsername() {
        // Given
//        Address address = new Address();
//        address.setStreet("Some Street");
//        address.setCity("Seoul");
//        address.setState("Some State");
//        address.setZipCode("12345");

        Member member = new Member();
        member.setUsername("testuser");
        member.setEmail("testuser@example.com");
        member.setPassword(new BCryptPasswordEncoder().encode("password"));
        member.setAddress(new HashSet<>());
        member.setRole(Role.USER);
        member.setPoints(10000);

        memberRepository.save(member);

        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertNotNull(userDetails.getPassword());

        assertTrue(new BCryptPasswordEncoder().matches("password", userDetails.getPassword()));

    }
}

