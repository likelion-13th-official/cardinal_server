package com.likelionsg13th.cardinal.security;

import com.likelionsg13th.cardinal.users.domain.Users;
import com.likelionsg13th.cardinal.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class GoogleOidcUserService implements org.springframework.security.oauth2.client.userinfo.OAuth2UserService<OidcUserRequest, OidcUser> {

    private final OidcUserService delegate = new OidcUserService();
    private final UserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest req) {
        log.debug("✅ GoogleOidcUserService 진입");
        OidcUser base = delegate.loadUser(req);

        Map<String, Object> claims = new LinkedHashMap<>(base.getClaims());
        String sub = asStr(claims.get("sub"));
        String name = asStr(claims.get("name"));
        String picture = asStr(claims.get("picture"));
        String email = asStr(claims.get("email"));

        // upsert
        Users user = userRepository.findByProviderAndProviderId("google", sub)
                .orElseGet(() -> Users.builder()
                        .provider("google")
                        .providerId(sub)
                        .nickname(name != null ? name : "사용자")
                        .profileImageUrl(picture)
                        .build());

        boolean dirty = false;
        if (name != null && !name.equals(user.getNickname())) {
            user.setNickname(name);
            dirty = true;
        }
        if (picture != null && !picture.equals(user.getProfileImageUrl())) {
            user.setProfileImageUrl(picture);
            dirty = true;
        }
        if (user.getId() == null || dirty) {
            userRepository.save(user);
            log.debug("📝 user upsert (google-oidc): providerId={}, dirty={}", sub, dirty);
        }

        // 정규화 속성 주입 (SuccessHandler에서 바로 사용 가능)
        claims.put("provider", "google");
        claims.put("providerId", sub);
        claims.put("nickname", user.getNickname());
        claims.put("profileImageUrl", user.getProfileImageUrl());

        Collection<? extends GrantedAuthority> authorities = base.getAuthorities();
        return new DefaultOidcUser(authorities, base.getIdToken(), base.getUserInfo(), "sub") {
            @Override
            public Map<String, Object> getClaims() {
                return Collections.unmodifiableMap(claims);
            }

            @Override
            public Map<String, Object> getAttributes() {
                return getClaims();
            }
        };

        }
        private static String asStr(Object obj) { return obj == null ? null : String.valueOf(obj); }
    }