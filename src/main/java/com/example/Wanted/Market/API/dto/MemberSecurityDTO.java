package com.example.Wanted.Market.API.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class MemberSecurityDTO implements OAuth2User {

    private String username;
    private String password;
    private String email;
    private boolean isDeleted;
    private boolean isSocial;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    // Constructor
    public MemberSecurityDTO(String username, String password, String email, boolean isDeleted, boolean isSocial, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isDeleted = isDeleted;
        this.isSocial = isSocial;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getName() {
        return this.username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

}
