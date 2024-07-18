package com.example.Wanted.Market.API.controller;

import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/register")
    public Member registerMember(@RequestBody Member member){
        return memberService.saveUser(member);
    }

    //로그인
    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
