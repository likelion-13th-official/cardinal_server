package com.likelionsg13th.cardinal.security;

import com.likelionsg13th.cardinal.users.domain.Users;
import com.likelionsg13th.cardinal.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {

        System.out.println("✅ KakaoOAuth2UserService 진입");
        System.out.println("Access Token = " + req.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = delegate.loadUser(req);
        Map<String, Object> attr = oAuth2User.getAttributes();

        String provider = "kakao";
        String providerId = String.valueOf(attr.get("id"));



        Map<String, Object> account = (Map<String, Object>) attr.getOrDefault("kakao_account", Map.of());
        Map<String, Object> profile = (Map<String, Object>) account.getOrDefault("profile", Map.of());
        String nickname = String.valueOf(
                profile.getOrDefault("nickname",
                        ((Map<String, Object>) attr.getOrDefault("properties", Map.of()))
                                .getOrDefault("nickname", "kakao-user"))
        );

        // 프로필 이미지 URL (우선순위: kakao_account.profile.profile_image_url -> properties.profile_image -> properties.thumbnail_image)
        String profileImageUrl = Optional.ofNullable(profile.get("profile_image_url"))
                .map(String::valueOf)
                .orElseGet(() -> {
                    Map<String, Object> properties = (Map<String, Object>) attr.getOrDefault("properties", Map.of());
                    return String.valueOf(
                            Optional.ofNullable(properties.get("profile_image"))
                                    .orElse(properties.getOrDefault("thumbnail_image", "")));
                });

        // upsert 형태로 저장/갱신
        Users user = userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> Users.builder()
                        .provider(provider)
                        .providerId(providerId)
                        .nickname(nickname)
                        .profileImageUrl(profileImageUrl) // 신규 가입 시 이미지 저장
                        .build()
                );

        boolean dirty = false;

        if (!Objects.equals(user.getNickname(), nickname)) {
            user.setNickname(nickname);
            dirty = true;
        }
        if (profileImageUrl != null && !profileImageUrl.isBlank()
                && !Objects.equals(user.getProfileImageUrl(), profileImageUrl)) {
            user.setProfileImageUrl(profileImageUrl);
            dirty = true;
        }

        if (user.getId() == null || dirty) {
            userRepository.save(user);
        }

        // 권한 미사용 → 빈 권한
        return new DefaultOAuth2User(List.of(), attr, "id");
    }
}