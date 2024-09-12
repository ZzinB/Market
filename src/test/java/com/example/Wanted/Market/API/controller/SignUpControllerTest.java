package com.example.Wanted.Market.API.controller;

import com.example.Wanted.Market.API.dto.UserFormDto;
import com.example.Wanted.Market.API.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SignUpController.class)
@WithMockUser(username = "user", roles = {"USER"})
class SignUpControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 폼 제출 시 MemberService 호출 및 리다이렉트 확인")
    public void testSignUpForm_Submission() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/sign-up")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("username", "JohnDoe")
                        .param("email", "john.doe@example.com")
                        .param("password", "Password1!")
                        .param("postcode", "12345")
                        .param("address", "123 Main St")
                        .param("detailAddress", "Apt 1B")
                        .param("extraAddress", "Building 3")
                        .param("nickname", "johndoe")
                        .param("phoneNumber", "123-456-7890"))
                .andExpect(status().is3xxRedirection()) // 리다이렉션 상태 코드 확인
                .andExpect(redirectedUrl("/home")); // 리다이렉트 URL 확인

        verify(memberService).registerMember(any(UserFormDto.class)); // registerMember 메소드 호출 검증
    }
}
