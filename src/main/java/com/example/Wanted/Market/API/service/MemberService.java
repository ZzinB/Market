package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.dto.UserFormDto;

public interface MemberService {
    Member registerMember(UserFormDto userFormDto);
    boolean authenticate(String username, String password);
}
