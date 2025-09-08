package com.likelionsg13th.cardinal.pubOffice;

import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

//    @PatchMapping("/{pubId}")
//    public ResponseEntity<ApiResponse> updateNoticeAndDescrption(@PathVariable Long pubId){
//
//    }
}