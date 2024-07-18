package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

//    private final MemberRepository memberRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
////    @Override
////    public void signup(Member member) {
////        member.setRole("USER");
////        memberRepository.save(member);
////    }
//    public Member registerUser(Member member) {
//        member.setPassword(passwordEncoder.encode(member.getPassword()));
//        return memberRepository.save(member);
//    }
//
//    //이미 가입된 회원일 경우 예외처리
//    private void validateDuplicateUser(Member user) {
//        Member findUser = memberRepository.findByEmail(user.getEmail());
//
//        if (findUser != null) {
//
//            throw new IllegalStateException("이미 가입된 회원입니다.");
//        }
//
//    public Member findByUsername(String username) {
//        return memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
//    }
//
//    @Override
//    public Member findByEmail(String email) {
//        return null;
//    }

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public Member saveUser(Member member) {
        // Member 저장 로직 구현
        return memberRepository.save(member);
    }

    @Override
    public Member getUserByEmail(String email) {
        // 이메일을 이용한 회원 조회 로직 구현
        return memberRepository.findByEmail(email);
    }
}
