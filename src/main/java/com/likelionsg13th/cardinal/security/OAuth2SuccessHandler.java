package com.likelionsg13th.cardinal.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelionsg13th.cardinal.security.jwt.JwtTokenProvider;
import com.likelionsg13th.cardinal.security.jwt.dto.TokenResponse;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwt;
    private static final ObjectMapper OM = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException {
  /*      OAuth2User principal = (OAuth2User) auth.getPrincipal();
        System.out.println("✅ OAuth2SuccessHandler 진입: " + auth.getName());


        // provider 구분 : kakao / google
*//*        String registrationId = (auth instanceof OAuth2AuthenticationToken o)
                ? o.getAuthorizedClientRegistrationId()
                : "unknown";

        String provider = registrationId.toLowerCase();*//*

        String provider = (auth instanceof OAuth2AuthenticationToken o)
                ? o.getAuthorizedClientRegistrationId()
                : (String) principal.getAttributes().get("provider");
        provider = provider == null ? "unknown" : provider.toLowerCase().trim();

        ParsedProfile profile = switch (provider) {
            case "kakao" -> parseKakao(principal.getAttributes());
            case "google" -> parseGoogle(principal.getAttributes());
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };


        // 2) providerId: 정규화 속성 우선, 없으면 auth.getName() (nameAttributeKey)
        String providerId = asStr(principal.getAttributes().get("providerId"));
        if (providerId == null) providerId = auth.getName(); // Kakao는 "providerId"를 name 키로 설정해둠
        // 마지막 폴백(혹시 정규화 없이 들어온 경우)
        if (providerId == null) {
            if ("google".equals(provider)) providerId = asStr(principal.getAttributes().get("sub"));
            if ("kakao".equals(provider))  providerId = asStr(principal.getAttributes().get("id"));
        }

        String subject = profile.provider + ":" + profile.providerId;
        System.out.println("✅ provider = " +  profile.provider);
        System.out.println("✅ providerID = " +  profile.providerId);*/


        OAuth2User principal = (OAuth2User) auth.getPrincipal();
        log.debug("✅ OAuth2SuccessHandler 진입: {}", auth.getName());

        // 1) provider: registrationId 우선, 없으면 정규화 attr(provider)
        String provider = (auth instanceof OAuth2AuthenticationToken o)
                ? o.getAuthorizedClientRegistrationId()
                : asStr(principal.getAttributes().get("provider"));
        provider = provider == null ? "unknown" : provider.toLowerCase().trim();

        // 2) providerId: 정규화 attr(providerId) 우선 → auth.getName() → 마지막 폴백(원본 키)
        String providerId = asStr(principal.getAttributes().get("providerId"));
        if (providerId == null) providerId = auth.getName(); // nameAttributeKey=providerId 로 설정해둠
        if (providerId == null) { // 정말 예외적인 폴백
            if ("google".equals(provider)) providerId = asStr(principal.getAttributes().get("sub"));
            if ("kakao".equals(provider))  providerId = asStr(principal.getAttributes().get("id"));
        }

        // 3) 표시용 프로필(정규화된 값)
        String nickname = asStr(principal.getAttributes().get("nickname"));
        String imageUrl = asStr(principal.getAttributes().get("profileImageUrl"));

        log.debug("✅ provider={}, providerId={}", provider, providerId);

        String subject = provider + ":" + providerId;
        String access  = jwt.createAccessToken(subject);
        String refresh = jwt.createRefreshToken(subject);

        System.out.println("-발급된 AccessToken = " + access);
        System.out.println("-발급된 RefreshToken = " + refresh);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("tokenType", "Bearer");
        body.put("accessToken", access);
        body.put("refreshToken", refresh);
        body.put("profile", Map.of(
                "provider", provider,
                "providerId", providerId
        ));

        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write(OM.writeValueAsString(body));

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

    private static String asStr(Object o) { return o == null ? null : String.valueOf(o); }
    private static String esc(String s) { return s == null ? "" : s.replace("\\","\\\\").replace("\"","\\\""); }
}