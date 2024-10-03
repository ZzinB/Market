package com.example.Wanted.Market.API.messaging.service;

import com.example.Wanted.Market.API.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class KakaoService {
//
//    private final HttpSession httpSession;
//    private final HttpCallService httpCallService;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
//    private String REST_API_KEY;
//
//    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
//    private String CLIENT_SECRET;
//
//    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
//    private String REDIRECT_URI;
//
//    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
//    private String AUTHORIZE_URI;
//
//    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
//    private String TOKEN_URI;
//
//    /**
//     * 카카오 OAuth 인증 페이지로 리다이렉트
//     */
//    public RedirectView goKakaoOAuth() {
//        return goKakaoOAuth("");
//    }
//
//    public RedirectView goKakaoOAuth(String scope) {
//        String uri = AUTHORIZE_URI + "?redirect_uri=" + REDIRECT_URI + "&response_type=code&client_id=" + REST_API_KEY;
//        if (!scope.isEmpty()) {
//            uri += "&scope=" + scope;
//        }
//
//        return new RedirectView(uri);
//    }
//    /**
//     * 카카오 OAuth 콜백 처리 및 액세스 토큰 추출
//     */
//    public RedirectView loginCallback(String code) {
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "authorization_code");
//        params.add("client_id", REST_API_KEY);
//        params.add("redirect_uri", REDIRECT_URI);
//        params.add("client_secret", CLIENT_SECRET);
//        params.add("code", code);
//
//        try {
//            // 토큰 요청
//           // String response = httpCallService.call(HttpMethod.POST, TOKEN_URI, null, params);
//
//            // JSON 응답에서 access_token 추출
//            JsonNode jsonNode = objectMapper.readTree(response);
//            String accessToken = jsonNode.get("access_token").asText();
//            httpSession.setAttribute("token", accessToken); // 세션에 저장
//            log.info("카카오 액세스 토큰 저장 완료: {}", accessToken);
//            return new RedirectView("/home.html");
//        } catch (Exception e) {
//            log.error("액세스 토큰 처리 중 오류 발생: {}", e.getMessage());
//            return new RedirectView("/error");
//        }
//    }
//    /**
//     * 카카오톡 메시지 전송
//     */
//    public String message() {
//        String uri = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
//        String accessToken = (String) httpSession.getAttribute("token");
//        if (accessToken == null) {
//            log.error("세션에 액세스 토큰이 없습니다.");
//            return "Access token not found";
//        }
//
//        try {
//            // 메시지 전송 호출
//            MultiValueMap<String, String> msgParams = Msg.default_msg_param();
//            return httpCallService.callWithToken(HttpMethod.POST, uri, accessToken, msgParams);
//        } catch (Exception e) {
//            log.error("메시지 전송 중 오류 발생: {}", e.getMessage());
//            return "Failed to send message";
//        }
//    }


