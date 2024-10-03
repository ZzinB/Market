package com.example.Wanted.Market.API.messaging.service;

import com.example.Wanted.Market.API.messaging.DefaultMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@Slf4j
public class MessageService extends HttpCallService{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String MSG_SEND_SERVICE_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private static final String SEND_SUCCESS_MSG = "메시지 전송에 성공했습니다.";
    private static final String SEND_FAIL_MSG = "메시지 전송에 실패했습니다.";

    private static final String SUCCESS_CODE = "0"; //kakao api에서 return해주는 success code 값

    public boolean sendMessage(String accessToken, DefaultMessageDto msgDto) {
        try {
            JSONObject linkObj = new JSONObject();
            linkObj.put("web_url", msgDto.getWebUrl());
            linkObj.put("mobile_web_url", msgDto.getMobileUrl());

            JSONObject templateObj = new JSONObject();
            templateObj.put("object_type", msgDto.getObjType());
            templateObj.put("text", msgDto.getText());
            templateObj.put("link", linkObj);
            templateObj.put("button_title", msgDto.getBtnTitle());

            HttpHeaders header = new HttpHeaders();
            header.set("Content-Type", APPLICATION_FORM_URLENCODED);
            header.set("Authorization", "Bearer " + accessToken);

            // API 호출을 위해 parameters 설정
            MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
            parameters.add("template_object", templateObj.toString());

            HttpEntity<?> messageRequestEntity = httpClientEntity(header, parameters);

//        String resultCode = "";
            ResponseEntity<String> response = httpRequest(MSG_SEND_SERVICE_URL, HttpMethod.POST, messageRequestEntity);
            log.info("카카오 API 응답: " + response.getBody());
            JSONObject jsonData = new JSONObject(response.getBody());
            String resultCode = jsonData.get("result_code").toString();

            return successCheck(resultCode);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    public boolean successCheck(String resultCode) {
        if(resultCode.equals(SUCCESS_CODE)) {
            logger.info(SEND_SUCCESS_MSG);
            return true;
        }else {
            logger.debug(SEND_FAIL_MSG);
            return false;
        }

    }
}