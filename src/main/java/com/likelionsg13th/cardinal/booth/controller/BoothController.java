package com.likelionsg13th.cardinal.booth.controller;

import com.likelionsg13th.cardinal.booth.dto.BoothDetailResponse;
import com.likelionsg13th.cardinal.booth.dto.BoothResponse;
import com.likelionsg13th.cardinal.booth.service.BoothService;
import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.common.dto.resonseDto.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booths")
@RequiredArgsConstructor
public class BoothController {
    private final BoothService boothService;
    /*전체 목록 조회*/
    @GetMapping
    public ResponseEntity<ApiResponse> getBoothList(
            @RequestParam("category") String category,
            @RequestParam(value = "isOperating", required = false) Boolean isOperating,
            @RequestParam(value = "day", required = false) String day){

        List<BoothResponse> boothList=boothService.getBoothList(category,isOperating,day);
        return ResponseEntity.ok(new ApiResponse(true,200,"부스 목록 조회 성공", boothList));

    }

    /* 개별 상세 조회*/
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getBoothDetail(
            @PathVariable long id
    ){
        BoothDetailResponse boothDetailResponse=boothService.getBoothDetail(id);
        return ResponseEntity.ok(new ApiResponse(true,200,"부스 개별 조회 성공", boothDetailResponse));
    }

    /* 검색*/
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchBooths(
            @RequestParam("query") String query,
            @RequestParam("page") int page
    ){
        PageDto<BoothResponse> response=boothService.searchBooths(query,page);
        return ResponseEntity.ok(new ApiResponse(true,200,"부스 검색 목록 조회 성공", response));
    }


}
