package com.example.Wanted.Market.API.controller;

import com.example.Wanted.Market.API.service.AlertNotificationServiceImpl;
import com.example.Wanted.Market.API.service.CustomOAuth2UserService;
import com.example.Wanted.Market.API.service.JwtService;
import com.example.Wanted.Market.API.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/auth")
@Log4j2
@RequiredArgsConstructor
public class LoginController {

    //private final KakaoAuthService kakaoAuthService;
    private final AlertNotificationServiceImpl alertNotificationService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final MemberServiceImpl memberService; // 회원 서비스
    private final JwtService jwtUtil; // JWT 토큰 제공자

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    private String KAKAO_AUTH_URL;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;

//
//    /**
//     * 카카오 로그인 페이지로 리다이렉트합니다.
//     */
//    @GetMapping("/kakao/login")
//    public void redirectToKakaoLogin(HttpServletResponse response) throws IOException {
//        String kakaoLoginUrl = KAKAO_AUTH_URL + "?response_type=code&client_id=" + KAKAO_CLIENT_ID + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8");
//        response.sendRedirect(kakaoLoginUrl);
//    }
//
//    /**
//     * 카카오 로그인 콜백을 처리하고 사용자 정보를 가져옵니다.
//     */
//    @GetMapping("/kakao/callback")
//    public ResponseEntity<String> handleKakaoCallback(@RequestParam String code) {
//        try {
//            // 카카오 인증 코드로 액세스 토큰 요청
//            String accessToken = kakaoAuthService.getAccessToken(code);
//
//            // 액세스 토큰으로 사용자 정보 가져오기
//            KakaoUserInfoDto kakaoUserInfo = kakaoAuthService.getKakaoUserInfo(accessToken);
//
//            // 사용자 처리 및 JWT 토큰 반환
//            String jwtToken = customOAuth2UserService.handleUser(kakaoUserInfo);
//
//            // JWT 토큰을 쿠키 또는 세션에 저장
//            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
//            if (response != null) {
//                Cookie jwtCookie = new Cookie("JWT", jwtToken);
//                jwtCookie.setHttpOnly(true);
//                jwtCookie.setPath("/");
//                response.addCookie(jwtCookie);
//            }
//
//            return ResponseEntity.ok(jwtToken);
//        } catch (Exception e) {
//            log.error("Kakao login failed", e);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Kakao login failed: " + e.getMessage());
//        }
//    }
//
//    /**
//     * 카카오톡 알림 전송을 처리합니다.
//     */
//    @PostMapping("/kakao/notify")
//    @Operation(summary = "Send Kakao Talk Notification", description = "Send a notification to a user via Kakao Talk")
//    public ResponseEntity<String> notifyUserOnSale(@RequestBody KakaoNotificationRequestDto requestDto) {
//        try {
//            String accessToken = requestDto.getToken();
//            String message = requestDto.getMessage();
//
//            boolean isSent = alertNotificationService.sendKakaoTalkNotification(accessToken, message);
//            if (isSent) {
//                return ResponseEntity.ok("Notification sent successfully");
//            } else {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send notification");
//            }
//        } catch (Exception e) {
//            log.error("Failed to send notification", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send notification");
//        }
//    }
}
