package com.likelionsg13th.cardinal.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelionsg13th.cardinal.security.jwt.JwtTokenProvider;
import com.likelionsg13th.cardinal.security.jwt.dto.TokenResponse;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwt;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException {
        if (!(auth instanceof OAuth2AuthenticationToken token)) {
            // 예상치 못한 타입 보호
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.setContentType("application/json;charset=UTF-8");
            res.getWriter().write("{\"error\":\"Unsupported authentication type\"}");
            return;
        }

        String registrationId = token.getAuthorizedClientRegistrationId();
        OAuth2User principal = (OAuth2User) auth.getPrincipal();
        System.out.println("✅ OAuth2SuccessHandler 진입: " + auth.getName());

        Object kakaoIdObj = principal.getAttribute("id");  // Long

        ProviderSubject providerSubject = extractProviderSubject(registrationId, principal.getAttributes());
        String provider = providerSubject.provider();      // "kakao" / "google"
        String providerId = providerSubject.providerId();  // kakao: "4389628977", google: "1087...." (sub)
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

    /**
     * 프로바이더별 사용자 고유 식별자 추출
     */
    private ProviderSubject extractProviderSubject(String registrationId, Map<String, Object> attributes) {
        if ("kakao".equalsIgnoreCase(registrationId)) {
            // 카카오는 최상위에 id(Long)가 옴
            Object idObj = attributes.get("id");
            if (idObj == null) {
                throw new IllegalStateException("Kakao attributes missing 'id'");
            }
            return new ProviderSubject("kakao", String.valueOf(idObj));
            }
        else if ("google".equalsIgnoreCase(registrationId)) {
            // 구글은 OIDC sub(String)
            Object sub = attributes.get("sub");
            if (sub == null) {
                throw new IllegalStateException("Google attributes missing 'sub'");
            }
            return new ProviderSubject("google", String.valueOf(sub));
            }
            else {
            // 기타 프로바이더를 확장하고 싶다면 여기에 분기 추가
            throw new IllegalStateException("Unsupported provider: " + registrationId);
        }
    }

    private record ProviderSubject(String provider, String providerId) {}
}
