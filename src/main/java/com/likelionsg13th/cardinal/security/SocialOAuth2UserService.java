package com.likelionsg13th.cardinal.security;

import com.likelionsg13th.cardinal.users.domain.Users;
import com.likelionsg13th.cardinal.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional // 저장(upsert) 있으므로 readOnly = false
public class SocialOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {


        log.debug("✅ SocialOAuth2UserService 진입");
        log.debug("Access Token(short) = {}", abbreviate(req.getAccessToken().getTokenValue(), 24));
        OAuth2User oAuth2User = delegate.loadUser(req);

        // provider: kakao / google
        String provider = req.getClientRegistration().getRegistrationId().toLowerCase();
        Map<String, Object> attr = oAuth2User.getAttributes();

//        String provider = "kakao";
//        String providerId = String.valueOf(attr.get("id"));

        Profile profile = switch (provider) {
            case "kakao"  -> parseKakao(attr);
            case "google" -> parseGoogle(attr);
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };


        /*Map<String, Object> account = (Map<String, Object>) attr.getOrDefault("kakao_account", Map.of());
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
*/

        // upsert 형태로 저장/갱신
        Users user = userRepository.findByProviderAndProviderId(profile.provider, profile.providerId)
                .orElseGet(() -> Users.builder()
                        .provider(profile.provider)
                        .providerId(profile.providerId)
                        .nickname(profile.nickname)
                        .profileImageUrl(profile.profileImg) // 신규 가입 시 이미지 저장
                        .build()
                );

        boolean dirty = false;

/*
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
*/


        if (profile.nickname != null && !profile.nickname.equals(user.getNickname())) { user.setNickname(profile.nickname); dirty = true; }
        if (profile.profileImg != null && !profile.profileImg.equals(user.getProfileImageUrl())) { user.setProfileImageUrl(profile.profileImg); dirty = true; }
        if (user.getId() == null || dirty) {
            userRepository.save(user);
            log.debug("📝 user upsert: provider={}, providerId={}, dirty={}", profile.provider, profile.providerId, dirty);
        }

        // 성공 핸들러가 쓰기 좋게 "정규화된" 속성으로 교체
        Map<String, Object> principalAttrs = Map.of(
                "provider", profile.provider,
                "providerId", profile.providerId,
                "nickname", user.getNickname(),
                "profileImageUrl", user.getProfileImageUrl()
        );
        // 권한 미사용 → 빈 권한, // 우리가 정규화한 속성들
        return new DefaultOAuth2User(List.of(), principalAttrs,"providerId");

    }


    private Profile parseGoogle(Map<String, Object> a) {
        String sub     = str(a.get("sub"));
        String name    = str(a.get("name"));
        String profileImg = str(a.get("picture"));
        return new Profile("google", sub, name,profileImg);
    }

    @SuppressWarnings("unchecked")
    private Profile parseKakao(Map<String, Object> a) {
        String id = str(a.get("id"));
        Map<String, Object> account = (Map<String, Object>) a.getOrDefault("kakao_account", Map.of());
        Map<String, Object> profile = (Map<String, Object>) account.getOrDefault("profile", Map.of());
        Map<String, Object> props   = (Map<String, Object>) a.getOrDefault("properties", Map.of());

        String nickname = firstNonNull(str(profile.get("nickname")), str(props.get("nickname")));
        String profileImg = firstNonNull(
                str(profile.get("profile_image_url")),
                firstNonNull(str(profile.get("thumbnail_image_url")), str(props.get("profile_image")))
        );

        return new Profile("kakao", id, nickname,profileImg);
    }

    private static String abbreviate(String v, int n) {
        if (v == null) return "null";
        if (v.length() <= n) return v;
        return v.substring(0, n) + "...";
    }

    private record Profile(String provider, String providerId, String nickname, String profileImg) {}
    private static String str(Object o){ return o==null? null : String.valueOf(o); }
    private static String firstNonNull(String a, String b){ return a!=null && !a.isBlank()? a : (b!=null && !b.isBlank()? b : null); }
}