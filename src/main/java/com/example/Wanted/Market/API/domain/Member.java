package com.example.Wanted.Market.API.domain;

import com.example.Wanted.Market.API.dto.UserFormDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
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
    private Set<Address> address = new HashSet<>();

    @OneToMany(mappedBy = "seller")
    private List<Item> items;

    @Enumerated(EnumType.STRING)
    private Role role;

    private double points;

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

}
