package com.likelionsg13th.cardinal.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelionsg13th.cardinal.security.jwt.JwtTokenProvider;
import com.likelionsg13th.cardinal.security.jwt.dto.TokenResponse;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwt;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException {
        OAuth2User principal = (OAuth2User) auth.getPrincipal();
        System.out.println("✅ OAuth2SuccessHandler 진입: " + auth.getName());


        // provider 구분 : kakao / google
        String registrationId = (auth instanceof OAuth2AuthenticationToken o)
                ? o.getAuthorizedClientRegistrationId()
                : "unknown";

        String provider = registrationId.toLowerCase();

        ParsedProfile p = switch (provider) {
            case "kakao" -> parseKakao(principal.getAttributes());
            case "google" -> parseGoogle(principal.getAttributes());
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };


        // provider별로 id/nickname/profileImage 파싱
        ParsedProfile profile = switch (provider) {
            case "kakao" -> parseKakao(principal.getAttributes());
            case "google" -> parseGoogle(principal.getAttributes());
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };

        String subject = profile.provider + ":" + profile.providerId;
        System.out.println("✅ provider = " +  profile.provider);

        String access  = jwt.createAccessToken(subject);
        String refresh = jwt.createRefreshToken(subject);

        System.out.println("-발급된 AccessToken = " + access);
        System.out.println("-발급된 RefreshToken = " + refresh);

        // JSON 응답
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("""
        {
          "tokenType":"Bearer",
          "accessToken":"%s",
          "refreshToken":"%s",
          "profile":{
            "provider":"%s",
          }
        }
        """.formatted(
                access, refresh,
                escape(p.provider)
        ));

    }

    // 구글
    private ParsedProfile parseGoogle(Map<String, Object> a) {
        // OpenID 표준: sub, name, picture, email
        String providerId = str(a.get("sub"));
        String nickname = str(a.get("name"));        // 닉네임으로 사용
        String profileImg = str(a.get("picture"));   // 프로필 이미지

        return new ParsedProfile("google", providerId, nickname, profileImg);
    }

    // 카카오
    @SuppressWarnings("unchecked")
    private ParsedProfile parseKakao(Map<String, Object> a) {
        // 구조: id, kakao_account: { profile: {nickname, profile_image_url, thumbnail_image_url}, ... }
        String providerId = str(a.get("id"));
        Map<String, Object> account = (Map<String, Object>) a.getOrDefault("kakao_account", Map.of());
        Map<String, Object> profile = (Map<String, Object>) account.getOrDefault("profile", Map.of());

        String nickname = str(profile.get("nickname")); // 닉네임
        String profileImg = str(profile.getOrDefault("profile_image_url",
                profile.getOrDefault("thumbnail_image_url", null))); // 프로필 이미지

        return new ParsedProfile("kakao", providerId, nickname, profileImg);
    }

    private record ParsedProfile(String provider, String providerId, String nickname, String profileImageUrl) {}

    private static String str(Object o) { return o == null ? null : String.valueOf(o); }

    private static String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}