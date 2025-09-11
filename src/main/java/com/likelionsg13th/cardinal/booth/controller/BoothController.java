package com.likelionsg13th.cardinal.booth.controller;

import com.likelionsg13th.cardinal.booth.dto.BoothDetailResponse;
import com.likelionsg13th.cardinal.booth.dto.BoothResponse;
import com.likelionsg13th.cardinal.booth.service.BoothService;
import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.common.dto.resonseDto.PageDto;
import com.likelionsg13th.cardinal.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booths")
@RequiredArgsConstructor
public class BoothController {
    private final BoothService boothService;
    private final UserService userService;
    /*전체 목록 조회*/
    @GetMapping
    public ResponseEntity<ApiResponse> getBoothList(
            @RequestParam("category") String category,
            @RequestParam(value = "isOperating", required = false) Boolean isOperating,
            @RequestParam(value = "day", required = false) String day,
            @RequestParam("page") int page,
            @AuthenticationPrincipal  UserDetails user){

        Long userId= userService.resolveUserIdOrNull(user);
        PageDto<BoothResponse> boothList=boothService.getBoothList(userId,category,isOperating,day,page);
        return ResponseEntity.ok(new ApiResponse(true,200,"부스 목록 조회 성공", boothList));

    }

    /* 개별 상세 조회*/
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getBoothDetail(
            @PathVariable long id,
            @AuthenticationPrincipal UserDetails user
    ){
        Long userId= userService.resolveUserIdOrNull(user);
        BoothDetailResponse boothDetailResponse=boothService.getBoothDetail(userId,id);
        return ResponseEntity.ok(new ApiResponse(true,200,"부스 개별 조회 성공", boothDetailResponse));
    }

    /* 검색*/
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchBooths(
            @RequestParam("query") String query,
            @RequestParam("page") int page,
            @AuthenticationPrincipal  UserDetails user
    ){
        Long userId= userService.resolveUserIdOrNull(user);
        PageDto<BoothResponse> response=boothService.searchBooths(userId,query,page);
        return ResponseEntity.ok(new ApiResponse(true,200,"부스 검색 목록 조회 성공", response));
    }


}
