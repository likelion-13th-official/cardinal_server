package com.likelionsg13th.cardinal.users.controller;

import com.likelionsg13th.cardinal.security.jwt.JwtTokenProvider;
import com.likelionsg13th.cardinal.security.jwt.dto.MessageResponse;
import com.likelionsg13th.cardinal.security.jwt.dto.TokenResponseDto;
import com.likelionsg13th.cardinal.users.domain.Users;
import com.likelionsg13th.cardinal.users.dto.requestDto.DummyLoginRequest;
import com.likelionsg13th.cardinal.users.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwt;
    private final UserRepository userRepository;


    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(HttpServletResponse res) {
        // 클라이언트에 저장된 토큰 쿠키 삭제 지시
        deleteCookie(res, "access_token");
        deleteCookie(res, "refresh_token");

        // 헤더로 토큰을 쓰는 앱이라면, 프론트가 로컬 저장소 토큰 삭제하도록 안내만 가능
        return ResponseEntity.ok(new MessageResponse("로그아웃 되었습니다."));
    }

    private void deleteCookie(HttpServletResponse res, String name) {
        Cookie c = new Cookie(name, null);
        c.setPath("/");           // 발급 시와 동일 path
        c.setHttpOnly(true);      // 발급 시와 동일 속성
        c.setSecure(true);        // HTTPS만
        c.setMaxAge(0);           // 즉시 만료
        res.addCookie(c);
    }


    @PostMapping("/dummy-login")
    public ResponseEntity<TokenResponseDto> dummyLogin(@RequestBody DummyLoginRequest request) {
    // Find or create a user based on the dummy request
        Users user = userRepository.findByProviderAndProviderId("kakao", request.getProviderId())
                .orElseGet(() -> {
                    Users newUser = Users.builder()
                          .provider("kakao")
                            .providerId(request.getProviderId())
                           .nickname(request.getNickname())
                            .profileImageUrl(null) // or a default image
                           .build();
                    return userRepository.save(newUser);
               });

        // Create JWTs for the found/created user
         String subject = "kakao:" + user.getProviderId();
       String accessToken = jwt.createAccessToken(subject);
       String refreshToken = jwt.createRefreshToken(subject);
        return ResponseEntity.ok(new TokenResponseDto(accessToken, refreshToken, "Bearer"));
     }
}