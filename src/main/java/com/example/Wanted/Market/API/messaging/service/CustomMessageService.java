package com.example.Wanted.Market.API.messaging.service;

import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.messaging.DefaultMessageDto;
import com.example.Wanted.Market.API.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomMessageService {

    @Autowired
    MessageService messageService;
    @Autowired
    private MemberRepository memberRepository;

    public boolean sendMyMessage() {
        DefaultMessageDto myMsg = new DefaultMessageDto();
        myMsg.setBtnTitle("자세히보기");
        myMsg.setMobileUrl("");
        myMsg.setObjType("text");
        myMsg.setWebUrl("");
        myMsg.setText("메시지 테스트입니다.");

        // 세션이나 DB에서 액세스 토큰을 직접 가져오는 로직
        String accessToken = getAccessToken();
        if (accessToken != null) {
            return messageService.sendMessage(accessToken, myMsg);
        }
        return false;
    }


    // 예시: 세션이나 DB에서 액세스 토큰을 가져오는 메서드
    private String getAccessToken() {
        // 예시로 세션에서 가져오는 경우
        // return session.getAttribute("kakaoAccessToken");

        // 또는, DB에서 가져오는 경우
        Optional<Member> memberOpt = memberRepository.findById(1L); // 임의의 사용자 ID로 검색
        if (memberOpt.isPresent()) {
            return memberOpt.get().getAccessToken();
        }
        return null;
    }
}