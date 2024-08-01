package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.dto.MemberDto;

public interface MemberService {
    //void signup(Member member);
//    Member findByUsername(String username);
//    Member findByEmail(String email);

//    Member saveUser(Member member);
//
//    Member getUserByEmail(String email);

    Member registerMember (MemberDto memberDto);
    boolean authenticate(String username, String password);
}
