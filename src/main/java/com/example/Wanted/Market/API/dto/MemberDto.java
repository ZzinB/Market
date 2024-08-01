package com.example.Wanted.Market.API.dto;

import com.example.Wanted.Market.API.domain.Role;

public class MemberDto {

    private String username;
    private String email;
    private String password;
    private Role role;

    // 기본 생성자
    public MemberDto() {
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
