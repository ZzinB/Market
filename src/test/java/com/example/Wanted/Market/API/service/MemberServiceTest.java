package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.dto.UserFormDto;
import com.example.Wanted.Market.API.repository.MemberRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MemberServiceTest {

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("회원가입 - 성공 케이스")
    void registerMember_Success() {
        // Given
        UserFormDto userFormDto = new UserFormDto();
        userFormDto.setName("홍길동");
        userFormDto.setEmail("test@example.com");
        userFormDto.setPassword("Test1234!");
        userFormDto.setPostcode("12345");
        userFormDto.setAddress("서울특별시 강남구");
        userFormDto.setDetailAddress("역삼동");
        userFormDto.setPhoneNumber("010-1234-5678");

        Member member = new Member();
        member.setUsername(userFormDto.getName());
        member.setEmail(userFormDto.getEmail());
        member.setPassword("encodedPassword");

        // When
        when(passwordEncoder.encode(userFormDto.getPassword())).thenReturn("encodedPassword");
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        Member registeredMember = memberService.registerMember(userFormDto);

        // Then
        assertNotNull(registeredMember);
        assertEquals("홍길동", registeredMember.getUsername());
        assertEquals("test@example.com", registeredMember.getEmail());
    }

    @Test
    @DisplayName("회원가입 - 중복 이메일")
    void registerMember_DuplicateEmail() {
        // Given
        UserFormDto userFormDto = new UserFormDto();
        userFormDto.setEmail("test@example.com");

        when(memberRepository.findByEmail(userFormDto.getEmail()))
                .thenReturn(Optional.of(new Member()));


        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberService.registerMember(userFormDto);
        });

        assertEquals("이미 사용 중인 이메일입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("회원가입 - 잘못된 비밀번호 형식")
    void registerMember_InvalidPassword() {
        // Given
        UserFormDto userFormDto = new UserFormDto();
        userFormDto.setPassword("weakpass");

        // When & Then
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            memberService.registerMember(userFormDto);
        });

        assertTrue(exception.getMessage().contains("비밀번호는 최소 8자, 대문자, 소문자, 숫자 및 특수문자를 포함해야 합니다."));
    }
}
