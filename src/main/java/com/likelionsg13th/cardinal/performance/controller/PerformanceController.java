package com.likelionsg13th.cardinal.performance.controller;


import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.common.dto.resonseDto.PageDto;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.common.enums.PerformanceCategory;
import com.likelionsg13th.cardinal.goods.dto.GoodsResponse;
import com.likelionsg13th.cardinal.performance.dto.PerformanceResponse;
import com.likelionsg13th.cardinal.performance.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/performances")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;


    @GetMapping
    public ResponseEntity<ApiResponse> getPerformanceList(
            @RequestParam("category") PerformanceCategory category,
            @RequestParam(value="day", required = false) DayOfWeek day
    ){
        List<PerformanceResponse> response = performanceService.getPerfromanceList(category, day);
        return ResponseEntity.ok(new ApiResponse(true, 200, "공연 목록 조회 성공", response));

    }

}
