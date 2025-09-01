package com.likelionsg13th.cardinal.users.service;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.UserNotFoundException;
import com.likelionsg13th.cardinal.security.jwt.JwtTokenProvider;
import com.likelionsg13th.cardinal.users.domain.Users;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public UserDto getMeBySubject(String subject) {
        String[] parts = parseSubject(subject); // [provider, providerId]
        String provider = parts[0];
        String providerId = parts[1];

        Users user = userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return UserDto.of(user);
    }

    private String[] parseSubject(String subject) {

        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("subject가 비어 있습니다.");
        }

/*        if (subject == null || !subject.contains(":")) {
            throw new IllegalArgumentException("subject 형식이 올바르지 않습니다. (예: kakao:{id})");
        }*/
        String[] parts = subject.split(":", 2);
        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new IllegalArgumentException("subject 형식이 올바르지 않습니다. (예:  kakao:123456, google:1234567890)");
        }

        return parts;
    }


    /*user 검증 메서드 */
    private Users checkUser(UserDto user) {
        return userRepository.findById(user.getId()).orElseThrow(() ->  new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}