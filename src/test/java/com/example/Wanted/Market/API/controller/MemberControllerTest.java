package com.example.Wanted.Market.API.controller;

import com.example.Wanted.Market.API.Payment.PaymentService;
import com.example.Wanted.Market.API.config.SecurityConfig;
import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.domain.Role;
import com.example.Wanted.Market.API.repository.MemberRepository;
import com.example.Wanted.Market.API.service.MemberService;
import com.example.Wanted.Market.API.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private MemberService memberService;

    @MockBean
    private ProductService productService;

    @MockBean
    private PaymentService paymentService;

    @BeforeEach
    public void setUp() {
        // Mock 데이터 초기화
        Member member = new Member();
        member.setUsername("testuser");
        member.setEmail("testuser@example.com");
        member.setPassword("password");
        member.setRole(Role.USER);

        Mockito.when(memberRepository.save(Mockito.any(Member.class))).thenReturn(member);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testMemberRegistration() throws Exception {
        // Prepare a new member to be registered
        Member newMember = new Member();
        newMember.setUsername("newuser");
        newMember.setEmail("newuser@example.com");
        newMember.setPassword("password");
        newMember.setRole(Role.USER);

        // Convert member object to JSON
        String memberJson = objectMapper.writeValueAsString(newMember);

        // Perform POST request to register the new member
        mockMvc.perform(post("/api/members/register")
                        .contentType("application/json")
                        .content(memberJson))
                .andExpect(status().isCreated()); // or status().isOk() depending on your API design
    }
}
