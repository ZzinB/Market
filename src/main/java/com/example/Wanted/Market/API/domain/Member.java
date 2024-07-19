package com.example.Wanted.Market.API.domain;

import com.example.Wanted.Market.API.dto.UserFormDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @OneToMany(mappedBy = "member")
    private Set<Address> address;

    @OneToMany(mappedBy = "seller")
    private List<Item> items;

    @Enumerated(EnumType.STRING)
    private Role role;

    /*
     * User엔티티를 생성하는 메서드
     * 유저 엔티티에 회원을 생성하는 메서드를 만들어서 관리를한다면
     * 코드가 변경되더라도 한 군데만 수정하면 되는 이점이 있다.
     * */
//    public static Member createUser(UserFormDto userFormDto, PasswordEncoder passwordEncoder) {
//        Member member = new Member();
//
//        member.setUsername(userFormDto.getName());
//        member.setEmail(userFormDto.getEmail());
//        member.setAddress(userFormDto.getAddress());
//
//        // 스프링 시큐리티 설정을 클래스에 등록한 BCryptPassword Bean을 파라미터로 넘겨서 비밀번호를 암호화
//        String password = passwordEncoder.encode(userFormDto.getPassword());
//        member.setPassword(password);
//        member.setRole(Role.USER);
//
//        return member;
//    }
}
