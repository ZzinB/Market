package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Address;
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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor

public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member registerMember(UserFormDto userFormDto) throws Exception {
        // 이메일 중복 체크
        if (memberRepository.findByEmail(userFormDto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        // 닉네임 중복 체크
        if (memberRepository.findByNickname(userFormDto.getNickname()).isPresent()) {
            throw new Exception("이미 존재하는 닉네임입니다.");
        }

        Member member = new Member();
        member.setUsername(userFormDto.getUsername());
        member.setEmail(userFormDto.getEmail());
        member.setPassword(passwordEncoder.encode(userFormDto.getPassword()));
        member.setNickname(userFormDto.getNickname());
        member.setPhoneNumber(userFormDto.getPhoneNumber());
        Address address = new Address();
        address.setStreet(userFormDto.getAddress()); // address -> street
        address.setCity(userFormDto.getDetailAddress()); // 도시 정보가 있으면 설정
        address.setState(userFormDto.getExtraAddress()); // 주 정보가 있으면 설정
        address.setZipCode(userFormDto.getPostcode()); // postcode -> zipCode

        // Address 객체를 Set으로 변환
        Set<Address> addresses = new HashSet<>();
        addresses.add(address);

        // Member에 주소 설정
        member.setAddress(addresses);
        member.setRole(Role.USER);
        member.setSocial(false);
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
