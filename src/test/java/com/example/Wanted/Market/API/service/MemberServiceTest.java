package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.dto.UserFormDto;
import com.example.Wanted.Market.API.exception.EmailAlreadyExistsException;
import com.example.Wanted.Market.API.exception.NicknameAlreadyExistsException;
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
import static org.mockito.Mockito.verify;
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
    void registerMember_Success() throws EmailAlreadyExistsException, NicknameAlreadyExistsException {
        // Given
        UserFormDto userFormDto = new UserFormDto();
        userFormDto.setUsername("홍길동");
        userFormDto.setEmail("test@example.com");
        userFormDto.setPassword("Test1234!");
        userFormDto.setPostcode("12345");
        userFormDto.setAddress("서울특별시 강남구");
        userFormDto.setDetailAddress("역삼동");
        userFormDto.setExtraAddress("Building 3");
        userFormDto.setNickname("hong");
        userFormDto.setPhoneNumber("010-1234-5678");

        when(memberRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(memberRepository.findByNickname(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Member member = memberService.registerMember(userFormDto);

        // Then
        verify(memberRepository).save(any(Member.class));
        assertNotNull(member);
        assertEquals("홍길동", member.getUsername());
        assertEquals("encodedPassword", member.getPassword());
        assertEquals("test@example.com", member.getEmail());
        assertNotNull(member.getAddress());
        assertFalse(member.getAddress().isEmpty());
    }

    @Test
    @DisplayName("회원가입 - 중복 이메일")
    void registerMember_DuplicateEmail() {
        // Given
        UserFormDto userFormDto = new UserFormDto();
        userFormDto.setEmail("test@example.com");

        when(memberRepository.findByEmail(userFormDto.getEmail())).thenReturn(Optional.of(new Member()));


        // When & Then
        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> memberService.registerMember(userFormDto));


        assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
    }
}
