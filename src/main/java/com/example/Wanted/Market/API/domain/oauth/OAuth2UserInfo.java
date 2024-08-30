package com.example.Wanted.Market.API.domain.oauth;

import java.util.Map;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId(); //소셜 식별 값 : 카카오 - "id", 네이버 - "id"

    public abstract String getNickname();

    public abstract String getEmail();

}
