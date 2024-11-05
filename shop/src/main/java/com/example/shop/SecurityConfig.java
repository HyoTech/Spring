package com.example.shop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final CustomOAuth2UserService customOAuth2UserService;
        private final CustomAuthFailureHandler customAuthFailureHandler;

        public SecurityConfig(CustomAuthFailureHandler customAuthFailureHandler,
                        CustomOAuth2UserService customOAuth2UserService) {
                this.customAuthFailureHandler = customAuthFailureHandler;
                this.customOAuth2UserService = customOAuth2UserService;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.csrf((csrf) -> csrf.disable());
                http.authorizeHttpRequests((authorize) -> authorize
                                .requestMatchers("/**").permitAll()
                                .requestMatchers("/oauth2/**").permitAll());

                // 폼 로그인 설정
                http.formLogin(formLogin -> formLogin
                                .loginPage("/login")
                                .defaultSuccessUrl("/")
                                .failureHandler(customAuthFailureHandler)
                                .permitAll());

                // OAuth2 로그인 설정
                http.oauth2Login(oauth2 -> oauth2
                                .loginPage("/login")
                                .defaultSuccessUrl("/", true) // 로그인 성공 후 이동할 URL 설정
                                .failureUrl("/error")
                                .userInfoEndpoint(userInfo -> userInfo
                                                .userService(customOAuth2UserService)));

                // 로그아웃 설정
                http.logout(logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login")
                                .permitAll());

                return http.build();

        }

        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}