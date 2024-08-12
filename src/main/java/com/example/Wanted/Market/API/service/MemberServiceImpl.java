package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.domain.Role;
import com.example.Wanted.Market.API.dto.UserFormDto;
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
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor

public class MemberServiceImpl implements MemberService, UserDetailsService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Member registerMember(UserFormDto userFormDto) {
        // MemberDto를 Member로 변환
        Member member = new Member();
        member.setUsername(userFormDto.getName());
        member.setEmail(userFormDto.getEmail());
        member.setPassword(passwordEncoder.encode(userFormDto.getPassword()));
        member.setRole(Role.USER);

        // Member 저장
        return memberRepository.save(member);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new User(member.getUsername(), member.getPassword(), new ArrayList<>());
    }

    @Override
    public boolean authenticate(String username, String password) {
        // Optional<Member>를 사용하여 회원 조회
        Optional<Member> optionalMember = memberRepository.findByUsername(username);

        // 회원이 존재하면 비밀번호를 확인하고, 존재하지 않으면 false 반환
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            return passwordEncoder.matches(password, member.getPassword());
        } else {
            return false; // 회원이 존재하지 않음
        }
    }
}
