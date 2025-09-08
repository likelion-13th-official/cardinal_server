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
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /*주점 관리자 전용 filterChain*/
    @Bean
    @Order(1)
    SecurityFilterChain pubAdminFilterChain(HttpSecurity http) throws Exception {

            http
                    .securityMatcher("/pubOffice/**") //  /pubOffice/ 로 시작하는 URL에만 적용
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/pubOffice/auth/login").permitAll() //로그인 경로는 해제
                            .anyRequest().authenticated() // /pubOffice/ 하위 모든 경로는 인증 필요
                    )
                    .sessionManagement(sm -> sm.sessionCreationPolicy(
                            org.springframework.security.config.http.SessionCreationPolicy.STATELESS
                    ))
                    .exceptionHandling(ex->ex
                            .authenticationEntryPoint(customAuthenticationEntryPoint))
                    .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                    .formLogin(form -> form.disable()) //  기본 FormLogin 비활성화
                    .httpBasic(httpBasic -> httpBasic.disable()); // 기본 HttpBasic 비활성화

            return http.build();

    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(
                        org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED
                ))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",
                                "/booths/**","/booths",
                                "/events/**", "/events",
                                "/goods/**", "/goods",
                                "/performances/**", "/performances",
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
