package com.likelionsg13th.cardinal.security;

import com.likelionsg13th.cardinal.auth.jwt.JwtAuthenticationFilter;
import com.likelionsg13th.cardinal.auth.jwt.JwtTokenProvider;
import com.likelionsg13th.cardinal.auth.oauth2.GoogleOidcUserService;
import com.likelionsg13th.cardinal.auth.oauth2.OAuth2SuccessHandler;
import com.likelionsg13th.cardinal.auth.service.SocialOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final SocialOAuth2UserService socialOAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final GoogleOidcUserService googleOidcUserService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public JwtAuthenticationFilter jwtFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**", 
                                "/booths/**",
                                "/events/**",
                                "/goods/**",
                                "/peformances/** ",
                                "/search/**", "/health",
                                "/login",               // 커스텀 로그인 페이지 자체는 허용
                                "/oauth2/**",           // OAuth2 흐름 허용
                                "/map/**",
                                "/css/**", "/js/**", "/images/**", "/webjars/**",
                                "/favicon.ico", "/default-ui.css").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(a -> a.baseUri("/oauth2/authorization"))
                        .redirectionEndpoint(r -> r.baseUri("/login/oauth2/code/*"))
                        .successHandler(successHandler)
                )
                .exceptionHandling(ex->ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

}