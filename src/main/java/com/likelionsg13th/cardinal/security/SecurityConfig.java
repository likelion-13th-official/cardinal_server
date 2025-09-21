package com.likelionsg13th.cardinal.security;

import com.likelionsg13th.cardinal.auth.jwt.JwtAuthenticationAdminFilter;
import com.likelionsg13th.cardinal.auth.jwt.JwtAuthenticationFilter;
import com.likelionsg13th.cardinal.auth.jwt.JwtTokenProvider;
import com.likelionsg13th.cardinal.auth.oauth2.GoogleOidcUserService;
import com.likelionsg13th.cardinal.auth.oauth2.OAuth2SuccessHandler;
import com.likelionsg13th.cardinal.auth.service.SocialOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity /*API 별 preAuthorize 사용 위함 By yeeun*/
public class SecurityConfig {


    private final JwtTokenProvider jwtTokenProvider;
    private final SocialOAuth2UserService socialOAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final GoogleOidcUserService googleOidcUserService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtAuthenticationAdminFilter jwtAuthenticationAdminFilter;

    @Bean
    public JwtAuthenticationFilter jwtFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider, customAuthenticationEntryPoint);
    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /* CORS 설정*/
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        //허용 주소,메서드,헤더
        config.setAllowedOrigins(List.of("http://localhost:5173", "https://sogang-cardinal.vercel.app", "https://www.2025cardinal.site" ));
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
    public FilterRegistrationBean<JwtAuthenticationAdminFilter> jwtAdminFilterRegistration(JwtAuthenticationAdminFilter filter) {
        FilterRegistrationBean<JwtAuthenticationAdminFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }


    /*주점 관리자 전용 filterChain*/
    /*@Bean
    @Order(1)
    SecurityFilterChain pubAdminFilterChain(HttpSecurity http) throws Exception {

            http
                    .securityMatcher("/pubOffice/**") //  /pubOffice/ 로 시작하는 URL에만 적용
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(
                                    "/pubOffice/auth/login","/pubOffice/auth/refresh").permitAll() //로그인,token 갱신 경로는 해제
                            .anyRequest().authenticated() // /pubOffice/ 하위 모든 경로는 인증 필요
                    )
                    .sessionManagement(sm -> sm.sessionCreationPolicy(
                            org.springframework.security.config.http.SessionCreationPolicy.STATELESS
                    ))
                    .exceptionHandling(ex->ex
                            .accessDeniedHandler(customAccessDeniedHandler))
                    .addFilterBefore(jwtAuthenticationAdminFilter,UsernamePasswordAuthenticationFilter.class)
                    .formLogin(form -> form.disable()) //  기본 FormLogin 비활성화
                    .httpBasic(httpBasic -> httpBasic.disable()); // 기본 HttpBasic 비활성화

            return http.build();

    }

    @Bean
    @Order(2)
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(
                        org.springframework.security.config.http.SessionCreationPolicy.STATELESS // ★
                ))
                .requestCache(rc -> rc.disable())
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
                                "/map/**","/admin/**",
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
    }*/


    @Bean @Order(1)
    SecurityFilterChain oauth2Chain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/oauth2/**", "/login/**", "/error")
                .csrf(csrf -> csrf.disable())
                .requestCache(rc -> rc.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(a -> a.anyRequest().permitAll())
                .oauth2Login(oauth -> {
                    oauth.authorizationEndpoint(a -> a.baseUri("/oauth2/authorization"));
                    oauth.redirectionEndpoint(r -> r.baseUri("/login/oauth2/code/*"));
                    oauth.userInfoEndpoint(ue -> ue
                            .userService(socialOAuth2UserService)
                            .oidcUserService(googleOidcUserService)
                    );
                    oauth.successHandler(successHandler);
                });
        return http.build();
    }

    @Bean @Order(2)
    SecurityFilterChain pubAdminChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/pubOffice/**")                  // ★ 관리자 전용 경로만!
                .csrf(csrf -> csrf.disable())
                .requestCache(rc -> rc.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/pubOffice/auth/login", "/pubOffice/auth/refresh").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationAdminFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .formLogin(f -> f.disable())
                .httpBasic(b -> b.disable());
        return http.build();
    }

    @Bean @Order(3)
    SecurityFilterChain apiChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")                            // ★ 나머지 전부 (예: /users/me)
                .csrf(csrf -> csrf.disable())
                .requestCache(rc -> rc.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(a -> a
                        .requestMatchers(
                                "/auth/**",
                                "/booths/**","/booths",
                                "/events/**","/events",
                                "/goods/**","/goods",
                                "/performances/**","/performances",
                                "/search/**","/health",
                                "/error", /*edit */
                                "/map/**", "/admin/**",
                                "/css/**","/js/**","/images/**","/webjars/**",
                                "/favicon.ico","/default-ui.css"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(customAuthenticationEntryPoint));
        return http.build();
    }


    private static String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

}
