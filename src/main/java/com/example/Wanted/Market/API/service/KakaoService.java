package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.domain.Role;
import com.example.Wanted.Market.API.dto.KakaoUserInfoDto;
import com.example.Wanted.Market.API.dto.MemberSecurityDTO;
import com.example.Wanted.Market.API.repository.MemberRepository;
import com.example.Wanted.Market.API.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class KakaoService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String KAKAO_TOKEN_URL;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String KAKAO_USER_INFO_URL;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("User Request: {}", userRequest);

        // DefaultOAuth2UserService를 사용해 사용자 정보를 불러옴
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        String email = getKakaoEmail(paramMap);
        log.info("Kakao Email: {}", email);

        return generateDTO(email, paramMap);
    }

    private MemberSecurityDTO generateDTO(String email, Map<String, Object> params) {
        Optional<Member> result = memberRepository.findByEmail(email);

        if (result.isEmpty()) {
            // 회원 추가
            String tempPassword = UUID.randomUUID().toString();
            Member member = Member.builder()
                    .username(email)
                    .password(passwordEncoder.encode(tempPassword))
                    .email(email)
                    .isSocial(true)
                    .role(Role.USER)
                    .build();
            memberRepository.save(member);

            return new MemberSecurityDTO(
                    email,
                    tempPassword,
                    email,
                    false,
                    true,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
        } else {
            Member member = result.get();
            List<SimpleGrantedAuthority> authorities = member.getRole() != null
                    ? Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + member.getRole().name()))
                    : Collections.emptyList();

            return new MemberSecurityDTO(
                    member.getUsername(),
                    member.getPassword(),
                    member.getEmail(),
                    member.isDel(),
                    member.isSocial(),
                    authorities
            );
        }
    }

    private String getKakaoEmail(Map<String, Object> paramMap) {
        log.info("Extracting Kakao Email");
        Map<String, Object> accountMap = (Map<String, Object>) paramMap.get("kakao_account");
        return (String) accountMap.get("email");
    }

    public String getToken(String code) throws JsonProcessingException {
        URI uri = UriComponentsBuilder.fromUriString(KAKAO_TOKEN_URL)
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
            return jsonNode.get("access_token").asText();
        } else {
            log.error("Failed to get access token: {}", response.getBody());
            throw new RuntimeException("Failed to get access token");
        }
    }

    public KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        URI uri = UriComponentsBuilder.fromUriString(KAKAO_USER_INFO_URL)
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

        RequestEntity<Void> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .build();

        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

            Long id = jsonNode.get("id").asLong();
            String nickname = jsonNode.path("properties").path("nickname").asText();
            String email = jsonNode.path("kakao_account").path("email").asText();

            return new KakaoUserInfoDto(id, nickname, email);
        } else {
            log.error("Failed to get user info: {}", response.getBody());
            throw new RuntimeException("Failed to get user info");
        }
    }

    public String handleUser(KakaoUserInfoDto kakaoUserInfo) {
        Long kakaoId = kakaoUserInfo.getId();
        String email = kakaoUserInfo.getEmail();
        String nickname = kakaoUserInfo.getNickname();

        Member member = memberRepository.findByEmail(email).orElse(null);

        if (member == null) {
            // 신규 회원 등록
            String tempPassword = UUID.randomUUID().toString();
            member = Member.builder()
                    .username(nickname)
                    .password(passwordEncoder.encode(tempPassword))
                    .email(email)
                    .nickname(nickname)
                    .isSocial(true)
                    .role(Role.USER)
                    .build();
            memberRepository.save(member);
        } else {
            // 기존 회원 업데이트
            member.setNickname(nickname);
            member.setSocial(true);
            memberRepository.save(member);
        }

        // JWT 토큰 생성
        return jwtUtil.generateToken(member.getUsername());
    }
}
