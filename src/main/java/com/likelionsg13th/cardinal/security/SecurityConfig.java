package com.likelionsg13th.cardinal.security;

import com.likelionsg13th.cardinal.security.jwt.JwtAuthenticationFilter;
import com.likelionsg13th.cardinal.security.jwt.JwtTokenProvider;
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

    @Bean
    public JwtAuthenticationFilter jwtFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( "/", "/health",
                                "/login",               // 커스텀 로그인 페이지 자체는 허용
                                "/oauth2/**",           // OAuth2 흐름 허용
                                "/css/**", "/js/**", "/images/**", "/webjars/**",
                                "/favicon.ico", "/default-ui.css").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(ae -> ae.baseUri("/oauth2/authorization"))
                        .redirectionEndpoint(re -> re.baseUri("/login/oauth2/code/*"))
                        .userInfoEndpoint(ue -> ue.userService(socialOAuth2UserService))
                        .successHandler(successHandler)
                        .failureHandler((req, res, ex) -> {
                            // 가장 중요한 한 줄: 정확한 실패 원인 로그
                            ex.printStackTrace(); // 또는 logger.error("OAuth2 login failed", ex);

                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json;charset=UTF-8");

                            String code = (ex instanceof org.springframework.security.oauth2.core.OAuth2AuthenticationException e)
                                    ? e.getError().getErrorCode()
                                    : ex.getClass().getSimpleName();

                            String desc = ex.getMessage();

                            res.getWriter().write("""
        {"ok":false,"stage":"oauth2Login","errorCode":"%s","message":"%s"}
        """.formatted(escape(code), escape(desc)));
                        })
                )
                .exceptionHandling(e -> e.authenticationEntryPoint(
                        new org.springframework.security.web.authentication.HttpStatusEntryPoint(
                                org.springframework.http.HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}