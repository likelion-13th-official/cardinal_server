package com.likelionsg13th.cardinal.auth.controller;

import com.likelionsg13th.cardinal.auth.dto.RefreshRequest;
import com.likelionsg13th.cardinal.auth.dto.TokenExchangeRequest;
import com.likelionsg13th.cardinal.auth.dto.TokenResponse;
import com.likelionsg13th.cardinal.auth.jwt.JwtTokenProvider;
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

    // 1) 원타임 코드 → 토큰 교환
    @PostMapping("/token/exchange")
    public ResponseEntity<ApiResponse> exchange(@Valid @RequestBody TokenExchangeRequest req) {
        TokenResponse tokens = authService.exchangeOneTimeCode(req.code());
        return ResponseEntity.ok(new ApiResponse(true, 200, "ok", tokens));
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



}