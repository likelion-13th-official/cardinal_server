package com.likelionsg13th.cardinal.users.service;

import com.likelionsg13th.cardinal.security.jwt.JwtTokenProvider;
import com.likelionsg13th.cardinal.users.domain.Users;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public UserDto getMeBySubject(String subject) {
        String[] parts = parseSubject(subject); // ["kakao", "{id}"]
        String provider = parts[0];
        String providerId = parts[1];

        // 카카오 연동 가정(필요 시 제거/확장)
        if (!"kakao".equalsIgnoreCase(provider)) {
            throw new IllegalArgumentException("카카오 로그인 사용자가 아닙니다.");
        }

        var user = userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return UserDto.of(user);
    }

    private String[] parseSubject(String subject) {
        if (subject == null || !subject.contains(":")) {
            throw new IllegalArgumentException("subject 형식이 올바르지 않습니다. (예: kakao:{id})");
        }
        String[] parts = subject.split(":", 2);
        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new IllegalArgumentException("subject 형식이 올바르지 않습니다. (예: kakao:{id})");
        }

        return parts;
    }
}