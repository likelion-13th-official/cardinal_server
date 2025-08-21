package com.likelionsg13th.cardinal.booth.controller;

import com.likelionsg13th.cardinal.booth.dto.BoothResponse;
import com.likelionsg13th.cardinal.booth.service.BoothService;
import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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



}
