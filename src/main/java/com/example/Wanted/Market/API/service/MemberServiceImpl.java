package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Address;
import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.domain.Role;
import com.example.Wanted.Market.API.dto.UserFormDto;
import com.example.Wanted.Market.API.exception.EmailAlreadyExistsException;
import com.example.Wanted.Market.API.exception.NicknameAlreadyExistsException;
import com.example.Wanted.Market.API.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor

public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member registerMember(UserFormDto userFormDto) throws EmailAlreadyExistsException, NicknameAlreadyExistsException {
        // 이메일 중복 체크
        if (memberRepository.findByEmail(userFormDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("이미 존재하는 이메일입니다.");
        }

        // 닉네임 중복 체크
        if (memberRepository.findByNickname(userFormDto.getNickname()).isPresent()) {
            throw new NicknameAlreadyExistsException("이미 존재하는 닉네임입니다.");
        }

        // 새로운 Member 객체 생성
        Member member = Member.builder()
                .username(userFormDto.getUsername())
                .email(userFormDto.getEmail())
                .password(passwordEncoder.encode(userFormDto.getPassword()))
                .nickname(userFormDto.getNickname())
                .phoneNumber(userFormDto.getPhoneNumber())
                .role(Role.USER)
                .isSocial(false)
                .build();

        // Address 객체 생성
        Address address = Address.builder()
                .street(userFormDto.getAddress())
                .city(userFormDto.getDetailAddress())
                .state(userFormDto.getExtraAddress())
                .zipCode(userFormDto.getPostcode())
                .member(member) // Address와 Member의 연결 설정
                .build();

        // Member 객체에 Address 추가
        member.getAddress().add(address);

        return memberRepository.save(member);
    }

    public Optional<Member> findByEmail(String email){
        return memberRepository.findByEmail(email);
    }

    public void updateMemberWithAdditionalInfo(String email, UserFormDto userFormDto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        // 추가 정보 업데이트 로직
        member.setUsername(userFormDto.getUsername());
        member.setPhoneNumber(userFormDto.getPhoneNumber());
        member.setPassword(passwordEncoder.encode(userFormDto.getPassword()));
        member.setRole(Role.USER);

        memberRepository.save(member);
    }
}
