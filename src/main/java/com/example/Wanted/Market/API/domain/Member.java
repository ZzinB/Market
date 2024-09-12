package com.example.Wanted.Market.API.domain;

import com.example.Wanted.Market.API.dto.UserFormDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    private String nickname;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Address> address = new ArrayList<>();

    @OneToMany(mappedBy = "seller")
    private List<Item> items;

    @Enumerated(EnumType.STRING)
    private Role role;

    private double points;

    private boolean isSocial;

    private boolean isDel;

    @Column(name = "access_token")
    private String accessToken;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; //KAKAO, NAVER

    private String socialId; //(일반 로그인인 경우 null)

    private String refreshToken; // 리프레시 토큰

    @Column(name = "phone_number")
    private String phoneNumber;

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    // 유저 권한 설정 메소드
    public void authorizeUser() {
        this.role = Role.USER;
    }

    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }
}
