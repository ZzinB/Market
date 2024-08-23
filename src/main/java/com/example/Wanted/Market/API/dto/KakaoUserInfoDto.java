package com.example.Wanted.Market.API.dto;

public class KakaoUserInfoDto {

    private final Long id;
    private final String nickname;
    private final String email;

    public KakaoUserInfoDto(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }
}

