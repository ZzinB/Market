package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.domain.Role;
import com.example.Wanted.Market.API.dto.MemberDto;
import com.example.Wanted.Market.API.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        // 초기 데이터 설정
        Member member = new Member();
        member.setUsername("newuser");
        member.setEmail("newuser@example.com");
        member.setPassword("encodedpassword");
        member.setRole(Role.USER);

        when(passwordEncoder.encode("password")).thenReturn("encodedpassword");
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
    }

    @Test
    public void testRegisterMember() {
        // Given
        MemberDto newMemberDto = new MemberDto();
        newMemberDto.setUsername("newuser");
        newMemberDto.setEmail("newuser@example.com");
        newMemberDto.setPassword("password");
        newMemberDto.setRole(Role.USER);

        // When
        Member member = memberService.registerMember(newMemberDto);

        // Then
        assertThat(member).isNotNull();
        assertThat(member.getUsername()).isEqualTo("newuser");
        assertThat(member.getEmail()).isEqualTo("newuser@example.com");
    }

    @Test
    public void testAuthenticateSuccess() {
        // Given
        String username = "newuser";
        String password = "$2a$10$W1x2M42zqa24a6xfvpzpgu7EuZOCJnDSq1nf1isaxEy35brt9V9ty";

        // When
        boolean isAuthenticated = memberService.authenticate(username, password);

        // Then
        assertThat(isAuthenticated).isTrue();
    }

    @Test
    public void testAuthenticateFailure() {
        // Given
        String username = "newuser";
        String password = "wrongpassword";

        // When
        boolean isAuthenticated = memberService.authenticate(username, password);

        // Then
        assertThat(isAuthenticated).isFalse();
    }
}
