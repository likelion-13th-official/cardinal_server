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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


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

    /* CORS 설정*/
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        //허용 주소,메서드,헤더
        config.setAllowedOrigins(List.of("http://localhost:5173"/*, "https://your-frontend.com" */));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));

        // 브라우저에 노출할 헤더
        config.setExposedHeaders(List.of("Authorization"));

        // 자격 증명(쿠키, 인증 헤더 등)
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 위 설정 적용
        return source;
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //cors 설정 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",
                                "/booths/**",
                                "/events/**",
                                "/goods/**",
                                "/performances/** ", "/performances",
                                "/search/**", "/health",
                                "/login",               // 커스텀 로그인 페이지 자체는 허용
                                "/oauth2/**",           // OAuth2 흐름 허용
                                "/map/**",
                                "/css/**", "/js/**", "/images/**", "/webjars/**",
                                "/favicon.ico", "/default-ui.css").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(ae -> ae.baseUri("/oauth2/authorization"))
                        .redirectionEndpoint(re -> re.baseUri("/login/oauth2/code/*"))
                        .userInfoEndpoint(ue -> ue.userService(socialOAuth2UserService).oidcUserService(googleOidcUserService)) // Google (OIDC))
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
                .exceptionHandling(ex->ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

}