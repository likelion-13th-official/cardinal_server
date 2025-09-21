package com.likelionsg13th.cardinal.pubOffice.controller;

import com.likelionsg13th.cardinal.auth.dto.RefreshRequest;
import com.likelionsg13th.cardinal.auth.dto.TokenResponse;
import com.likelionsg13th.cardinal.auth.service.AuthService;
import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.pubOffice.dto.PubAdminDto;
import com.likelionsg13th.cardinal.pubOffice.dto.request.PubAdminLogin;
import com.likelionsg13th.cardinal.pubOffice.dto.request.UpdateNoticeAndDescripDto;
import com.likelionsg13th.cardinal.pubOffice.dto.response.PubAdminInfoResponse;
import com.likelionsg13th.cardinal.pubOffice.dto.response.PubAdminLoginResposne;
import com.likelionsg13th.cardinal.pubOffice.dto.response.UpdatePubResponse;
import com.likelionsg13th.cardinal.pubOffice.service.CustomUserDetails;
import com.likelionsg13th.cardinal.pubOffice.service.PubAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pubOffice")
public class  PubOfficeController {

    private final PubAdminService pubAdminService;
    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse> login(@RequestBody PubAdminLogin loginDto) {

        PubAdminLoginResposne resposne =  pubAdminService.pubAdminLogin(loginDto);
        return ResponseEntity.ok(new ApiResponse(true, 200, "주점 관리자 로그인 성공",resposne ));
    }

    @PatchMapping("/{pubId}")
    @PreAuthorize("@pubAdminService.canUpdate(principal, #pubId)")
    public ResponseEntity<ApiResponse> updateNoticeAndDescription (@PathVariable Long pubId,
                                                              @AuthenticationPrincipal CustomUserDetails principal,
                                                              @RequestBody @Valid  UpdateNoticeAndDescripDto updateNoticeAndDescripDto){

         UpdatePubResponse response = pubAdminService.updateNoticeAndDescription(pubId,updateNoticeAndDescripDto);
        return ResponseEntity.ok(new ApiResponse(true, 200, "주점 공지사항,소개글 수정 성공",response ));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ApiResponse> refresh(@Valid @RequestBody RefreshRequest req) {
        TokenResponse res  = authService.adminRefresh(req.getRefreshToken());
        return ResponseEntity.ok(new ApiResponse(true, 200, "refresh token refreshed",res));
    }

    /*몇번 관리자인지, department */
    @GetMapping("/auth/me")
    public ResponseEntity<ApiResponse> refresh(@AuthenticationPrincipal CustomUserDetails principal) {

        PubAdminInfoResponse response = pubAdminService.checkAdminInfo(principal);

        return ResponseEntity.ok(new ApiResponse(true, 200, "주점 관리자 정보 ",response));
    }

}