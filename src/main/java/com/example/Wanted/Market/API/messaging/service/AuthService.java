package com.example.Wanted.Market.API.messaging.service;

import com.example.Wanted.Market.API.domain.Member;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class AuthService extends HttpCallService{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String AUTH_URL = "https://kauth.kakao.com/oauth/token";

    public static String authToken;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String REST_API_KEY;
    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String GRANT_TYPE;

    public String  getKakaoAuthToken(String code)  {
        HttpHeaders headers = new HttpHeaders();
        String accessToken = "";
        String refrashToken = "";

        //headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Content-Type", APPLICATION_FORM_URLENCODED);
        //headers.add("Accept", "application/json");

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("code", code);
        parameters.add("grant_type", GRANT_TYPE);
        parameters.add("client_id", REST_API_KEY);
        parameters.add("redirect_uri", REDIRECT_URI);
        // parameters.add("client_secret", CLIENT_SECRET);

        HttpEntity<?> requestEntity = httpClientEntity(headers, parameters);

        ResponseEntity<String> response = httpRequest(AUTH_URL, HttpMethod.POST, requestEntity);
        JSONObject jsonData = new JSONObject(response.getBody());
        accessToken = jsonData.get("access_token").toString();
        refrashToken = jsonData.get("refresh_token").toString();
//        if(accessToken.isEmpty() || refrashToken.isEmpty()) {
//            logger.debug("토큰발급에 실패했습니다.");
//            return false;
//        }else {
//            authToken = accessToken;
//            return true;
//        }
        return accessToken;
    }
}