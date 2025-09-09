package com.likelionsg13th.cardinal.pubOffice.controller;

import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.pubOffice.dto.PubAdminDto;
import com.likelionsg13th.cardinal.pubOffice.dto.request.PubAdminLogin;
import com.likelionsg13th.cardinal.pubOffice.dto.request.UpdateNoticeAndDescripDto;
import com.likelionsg13th.cardinal.pubOffice.dto.response.PubAdminLoginResposne;
import com.likelionsg13th.cardinal.pubOffice.dto.response.UpdatePubResponse;
import com.likelionsg13th.cardinal.pubOffice.service.PubAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pubOffice")
public class PubOfficeController {

    private final PubAdminService pubAdminService;

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse> login(@RequestBody PubAdminLogin loginDto) {

        PubAdminLoginResposne resposne =  pubAdminService.pubAdminLogin(loginDto);
        return ResponseEntity.ok(new ApiResponse(true, 200, "주점 관리자 로그인 성공",resposne ));
    }

    @PatchMapping("/{pubId}")
    @PreAuthorize("@pubAdminService.canUpdate(principal,#pubId)") /* canUpdate 메서드를 호출해서 권한을 미리 확인*/
    public ResponseEntity<ApiResponse> updateNoticeAndDescription (@PathVariable Long pubId,
                                                              @AuthenticationPrincipal UserDetails principal,
                                                              @RequestBody @Valid  UpdateNoticeAndDescripDto updateNoticeAndDescripDto){


        UpdatePubResponse response = pubAdminService.updateNoticeAndDescription(pubId,principal,updateNoticeAndDescripDto);
        return ResponseEntity.ok(new ApiResponse(true, 200, "주점 공지사항,소개글 수정 성공",response ));
    }
}