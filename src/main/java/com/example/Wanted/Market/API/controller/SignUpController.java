package com.example.Wanted.Market.API.controller;

import com.example.Wanted.Market.API.oauth2.domain.oauth.CustomOAuth2User;
import com.example.Wanted.Market.API.dto.UserFormDto;
import com.example.Wanted.Market.API.exception.EmailAlreadyExistsException;
import com.example.Wanted.Market.API.exception.NicknameAlreadyExistsException;
import com.example.Wanted.Market.API.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class SignUpController {

    private final MemberService memberService;

    @GetMapping("/oauth2/sign-up")
    public String oauth2SignUp(Model model) {
        model.addAttribute("userFormDto", new UserFormDto());
        return "oauth2-sign-up";
    }

    @PostMapping("/oauth2/sign-up")
    public String processOauth2SignUp(@ModelAttribute UserFormDto userFormDto, HttpSession session) {
        try {
            // 세션에서 소셜 로그인된 사용자 정보를 가져옴
            CustomOAuth2User oAuth2User = (CustomOAuth2User) session.getAttribute("oauthUser");

            if (oAuth2User != null) {
                // 소셜 로그인 회원 정보를 기반으로 추가 정보를 업데이트
                memberService.updateMemberWithAdditionalInfo(oAuth2User.getEmail(), userFormDto);

                // 세션에서 사용자 정보를 제거
                session.removeAttribute("oauthUser");
            }
            return "redirect:/home"; // 회원가입 성공 후 /home으로 리다이렉트
        } catch (Exception e) {
            return "redirect:/oauth2/sign-up?error"; // 에러 발생 시 다시 회원가입 폼으로 리다이렉트
        }
    }

    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute("userFormDto", new UserFormDto());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String processSignUp(@ModelAttribute UserFormDto userFormDto, RedirectAttributes redirectAttributes) {
        try {
            memberService.registerMember(userFormDto); // 회원가입 로직
            return "redirect:/home"; // 회원가입 성공 후 /home으로 리다이렉트
        } catch (EmailAlreadyExistsException | NicknameAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/sign-up"; // 에러 발생 시 다시 회원가입 폼으로 리다이렉트
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "회원가입 중 오류가 발생했습니다. 다시 시도해 주세요.");
            return "redirect:/sign-up";
        }
    }
}
