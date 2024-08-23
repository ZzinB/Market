package com.example.Wanted.Market.API.controller;

import com.example.Wanted.Market.API.dto.KakaoUserInfoDto;
import com.example.Wanted.Market.API.service.KakaoService;
import com.example.Wanted.Market.API.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/kakao")
@Tag(name = "Kakao Authentication", description = "Kakao OAuth2 Authentication API")
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;

    @GetMapping("/login")
    @Operation(summary = "Kakao Login", description = "Authenticate with Kakao and return a JWT token")
    @ApiResponse(responseCode = "200", description = "Successful Kakao login")
    @ApiResponse(responseCode = "400", description = "Bad request")
    public ResponseEntity<String> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws Exception {
        try {
            // 카카오 인증 코드로 액세스 토큰 요청
            String accessToken = kakaoService.getToken(code);

            // 액세스 토큰으로 사용자 정보 가져오기
            KakaoUserInfoDto kakaoUserInfo = kakaoService.getKakaoUserInfo(accessToken);

            // 사용자 처리 및 JWT 토큰 반환
            String jwtToken = kakaoService.handleUser(kakaoUserInfo);

            // JWT 토큰을 응답 본문으로 반환
            return ResponseEntity.ok(jwtToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Kakao login failed: " + e.getMessage());
        }
    }
}

