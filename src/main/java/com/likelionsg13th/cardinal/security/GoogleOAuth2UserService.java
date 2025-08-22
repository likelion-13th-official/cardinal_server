/*
package com.likelionsg13th.cardinal.security;

import com.likelionsg13th.cardinal.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    // 필요 시, UserRepository 등 주입
    // private final UserRepository userRepository;

    private static final String REGISTRATION_ID = "google";
    private static final String NAME_ATTRIBUTE_KEY = "sub";

    private final DefaultOAuth2UserService defaultService = new DefaultOAuth2UserService();
    private final OidcUserService oidcService = new OidcUserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        if (!REGISTRATION_ID.equalsIgnoreCase(registrationId)) {
            // 혹시 다른 provider가 잘못 연결되면 방어
            return defaultService.loadUser(userRequest);
        }

        // OIDC 스코프(openid)가 포함되었다면 OIDC 방식
        boolean isOidc = userRequest.getClientRegistration()
                .getScopes().stream().anyMatch("openid"::equalsIgnoreCase);

        Map<String, Object> attributes;
        Collection<? extends GrantedAuthority> authorities;

        if (isOidc) {
            OidcUser oidcUser = oidcService.loadUser(new OidcUserRequest(
                    userRequest.getClientRegistration(),
                    userRequest.getAccessToken(),
                    userRequest.getIdToken()
            ));
            attributes = new LinkedHashMap<>(oidcUser.getAttributes());
            authorities = oidcUser.getAuthorities();
        } else {
            OAuth2User oAuth2User = defaultService.loadUser(userRequest);
            attributes = new LinkedHashMap<>(oAuth2User.getAttributes());
            authorities = oAuth2User.getAuthorities();
        }

        // 필수 키 보정: sub가 반드시 있어야 함
        Object sub = attributes.get(NAME_ATTRIBUTE_KEY);
        if (sub == null) {
            throw new IllegalStateException("Google user attributes missing 'sub'");
        }

        // (선택) 여기에 사용자 upsert 로직 추가 (email 기반 등)
        // String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");
//        Users user = userRepository.upsertFromGoogle(sub.toString(), name, picture);

        // 기본 권한 부여 (필요 시 ROLE_USER 등)
        if (authorities == null || authorities.isEmpty()) {
            authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }

        // DefaultOAuth2User로 감싸서 SecurityContext에 올라가게 함
        return new DefaultOAuth2User(authorities, attributes, NAME_ATTRIBUTE_KEY);
    }
}*/
