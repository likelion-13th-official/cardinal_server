package com.likelionsg13th.cardinal.users.controller;

import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> me(Authentication authentication, @AuthenticationPrincipal UserDetails principal) {
/*
        if (principal == null) {
            // 보통 Security filter에서 401로 처리되지만 방어적으로 체크
            throw new IllegalStateException("인증 정보가 없습니다.");
        }
*/


        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("인증 정보가 없습니다.");
        }

        //String subject = resolveSubject(authentication);
        System.out.println("REQUEST_SUBJECT=" + principal.getUsername()); // ★ 여기!

        // subject 예: "kakao:4389628977" 또는 "google:109876543210987654321"
        UserDto user= userService.getMeBySubject(principal.getUsername()); // "kakao:{id}"

        return ResponseEntity.ok(new ApiResponse(true, 200, "user 조회 성공", user));
    }
}