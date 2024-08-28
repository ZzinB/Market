package com.example.Wanted.Market.API.config;

import com.example.Wanted.Market.API.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${remember.me.key}")
    private String rememberMeKey;

    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, UserDetailsService customUserDetailsService) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/auth/**", "/oauth2/**") // API와 OAuth2 로그인은 CSRF 비활성화
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/auth/**", "/oauth2/**", "/", "/login", "/oauth2/authorization/kakao", "/home").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/api/auth/login") // 로그인 페이지
                        .defaultSuccessUrl("/home", true) // 로그인 성공 후 리다이렉트 URL
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // 사용자 정보 처리
                        )
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/api/auth/login")
                                .defaultSuccessUrl("/home", true)
                                .permitAll()
                )
                .logout(logout -> logout
                        .permitAll()
                )
                .rememberMe(rememberMe ->
                        rememberMe
                                .tokenRepository(inMemoryTokenRepository())
                                .tokenValiditySeconds(86400)
                                .key(rememberMeKey)
                                .userDetailsService(customUserDetailsService)
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                                .maximumSessions(1)
                                .expiredUrl("/login?expired")
                );

        return http.build();
    }

    @Bean
    public InMemoryTokenRepositoryImpl inMemoryTokenRepository() {
        return new InMemoryTokenRepositoryImpl();
    }
}
