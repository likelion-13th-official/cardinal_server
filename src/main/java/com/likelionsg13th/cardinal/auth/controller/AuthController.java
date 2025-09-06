package com.likelionsg13th.cardinal.auth.controller;

import com.likelionsg13th.cardinal.auth.dto.RefreshRequest;
import com.likelionsg13th.cardinal.auth.dto.TokenResponse;
import com.likelionsg13th.cardinal.auth.jwt.JwtTokenProvider;
import com.likelionsg13th.cardinal.auth.dto.MessageResponse;
import com.likelionsg13th.cardinal.auth.dto.TokenResponseDto;
import com.likelionsg13th.cardinal.auth.service.AuthService;
import com.likelionsg13th.cardinal.auth.service.OneTimeCodeService;
import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.users.domain.Users;
import com.likelionsg13th.cardinal.users.dto.request.DummyLoginRequest;
import com.likelionsg13th.cardinal.users.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwt;
    private final AuthService authService;
    private final OneTimeCodeService codeService;

    // 나중에 삭제해주세요 (@윤예은)
    private final UserRepository userRepository;


    // 1) 원타임 코드 → 토큰 교환
    @PostMapping("/token/exchange")
    public ResponseEntity<?> exchange(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        String subject = codeService.consume(code);
        if (subject == null) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "message", "invalid_or_expired_code"));
        }
        TokenResponse tokens = authService.issueToken(subject);
        return ResponseEntity.ok(Map.of(
                "ok", true,
                "tokenType", "Bearer",
                "accessToken", tokens.getAccessToken(),
                "refreshToken", tokens.getRefreshToken()
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refresh(@Valid @RequestBody RefreshRequest req) {
        TokenResponse res  = authService.refresh(req.getRefreshToken());
        return ResponseEntity.ok(new ApiResponse(true, 200, "refresh token refreshed",res));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@Valid @RequestBody RefreshRequest req) {
        authService.logoutByRefresh(req.getRefreshToken());
        return ResponseEntity.ok(new ApiResponse(true, 200, "Logout successful",null));
    }

/*
    private void deleteCookie(HttpServletResponse res, String name) {
        Cookie c = new Cookie(name, null);
        c.setPath("/");           // 발급 시와 동일 path
        c.setHttpOnly(true);      // 발급 시와 동일 속성
        c.setSecure(true);        // HTTPS만
        c.setMaxAge(0);           // 즉시 만료
        res.addCookie(c);
    }
*/


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