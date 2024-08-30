package com.example.Wanted.Market.API.security;

import com.example.Wanted.Market.API.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CustomUserDetails implements OAuth2User, UserDetails {

    private final Member member;
    private final Map<String, Object> attributes;

    public CustomUserDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + member.getRole().name()));
    }

    @Override
    public String getName() {
        return member.getUsername();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 사용자 계정이 만료되지 않았다고 가정
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 사용자 계정이 잠겨있지 않았다고 가정
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 사용자 자격 증명이 만료되지 않았다고 가정
    }

    @Override
    public boolean isEnabled() {
        return !member.isDel(); // 계정이 삭제되지 않았는지 확인
    }

}
