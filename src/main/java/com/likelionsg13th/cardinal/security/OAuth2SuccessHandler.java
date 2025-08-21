package com.likelionsg13th.cardinal.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelionsg13th.cardinal.security.jwt.JwtTokenProvider;
import com.likelionsg13th.cardinal.security.jwt.dto.TokenResponse;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwt;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException {
        OAuth2User principal = (OAuth2User) auth.getPrincipal();
        System.out.println("✅ OAuth2SuccessHandler 진입: " + auth.getName());

        Object kakaoIdObj = principal.getAttribute("id");  // Long
        String provider = "kakao";
        String providerId = String.valueOf(kakaoIdObj);    // "4389628977" 로 안전 변환
        String subject = provider + ":" + providerId;
        System.out.println("카카오 providerId = " + providerId);

        String access = jwt.createAccessToken(subject);
        String refresh = jwt.createRefreshToken(subject);

        System.out.println("발급된 AccessToken = " + access);
        System.out.println("발급된 RefreshToken = " + refresh);

        //직렬화 응답
/*        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("""
        {"accessToken":"%s","refreshToken":"%s","tokenType":"Bearer"}
        """.formatted(access, refresh));*/

        //json 응답
        TokenResponse tokenRes = new TokenResponse(access, refresh);
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json;charset=UTF-8");
        new ObjectMapper().writeValue(res.getWriter(), tokenRes);
    }
}
