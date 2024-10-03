package com.example.Wanted.Market.API.messaging;

import com.example.Wanted.Market.API.messaging.service.AuthService;
import com.example.Wanted.Market.API.messaging.service.CustomMessageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//@RestController
//public class KakaoController {
//
//    @Autowired
//    AuthService authService;
//
//    @Autowired
//    CustomMessageService customMessageService;
//
//    @GetMapping("/login/oauth2/code/kakao")
//    public String serviceStart(String code) {
//        if(authService.getKakaoAuthToken(code)) {
//            customMessageService.sendMyMessage();
//            return "메시지 전송 성공";
//        }else {
//            return "토큰발급 실패";
//        }
//    }
//}
@RestController
@RequestMapping("/")
public class KakaoController {

    @Autowired
    AuthService authService;

    @Autowired
    CustomMessageService customMessageService;

    @GetMapping("/login/oauth2/code/kakao")
    public String kakaoLogin(@RequestParam("code") String code, HttpSession session) {
        String accessToken = authService.getKakaoAuthToken(code);
        if (accessToken != null) {
            // 액세스 토큰을 세션에 저장
            session.setAttribute("accessToken", accessToken);
            // 이 방법은 HttpSession을 사용하여 스레드 안전하게 관리 가능
            return "로그인 성공 ! 토큰 : " + accessToken;
        }
        return "토큰 발급 실패";
    }

    /*
    @PostMapping("/kakao/send-message")
    public String sendKakaoMessage(HttpSession session) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            boolean success = customMessageService.sendMyMessage(accessToken);
            return success ? "메시지 전송 성공" : "메시지 전송 실패";
        }
        return "액세스 토큰이 없습니다.";
    }

     */

//    @PostMapping("/kakao/send-message/{memberName}")
//    public String sendKakaoMessage(@PathVariable String memberName) {
//        System.out.println(memberName);
//        boolean success = customMessageService.sendMyMessage(memberName);
//        return success ? "메시지 전송 성공" : "메시지 전송 실패";
//    }
}