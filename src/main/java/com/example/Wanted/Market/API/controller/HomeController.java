package com.example.Wanted.Market.API.controller;

import com.example.Wanted.Market.API.domain.Member;
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
    public String homePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Member> memberOpt = memberService.findByUsername(username);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            model.addAttribute("username", member.getUsername());
        } else {
            model.addAttribute("username", "사용자를 찾을 수 없습니다");
        }
        return "home";
    }
}

