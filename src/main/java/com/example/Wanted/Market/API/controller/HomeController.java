package com.example.Wanted.Market.API.controller;

import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.domain.oauth.CustomOAuth2User;
import com.example.Wanted.Market.API.service.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private MemberServiceImpl memberService;

    @GetMapping("/home")
    public String homePage(Model model, Authentication authentication) {
        // 인증된 사용자 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail(); // 이메일로 사용자 찾기

        Optional<Member> memberOpt = memberService.findByEmail(email);

        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            model.addAttribute("username", member.getUsername()); // 데이터베이스에서 저장된 username을 모델에 추가
        } else {
            model.addAttribute("username", "사용자를 찾을 수 없습니다");
        }
        return "home";
    }
}