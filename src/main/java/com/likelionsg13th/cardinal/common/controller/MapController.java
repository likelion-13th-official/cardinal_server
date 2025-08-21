package com.likelionsg13th.cardinal.common.controller;

import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapDetailDto;
import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapListDetailDto;
import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapListDto;
import com.likelionsg13th.cardinal.common.exception.InvalidParameterException;
import com.likelionsg13th.cardinal.common.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @GetMapping("/detail")
    public ResponseEntity<ApiResponse> getDetail(@RequestParam String category,
                                                 @RequestParam(required = false) Long id){

        if(category == null){ throw new InvalidParameterException("category is null"); }

        MapDetailDto response = mapService.viewDetail(category,id);

        return ResponseEntity.ok(new ApiResponse(true,200,"지도 상세 페이지 조회 성공",response));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getList(@RequestParam String category,
                                               @RequestParam(required = false) Long buildingId){

        MapListDto response = mapService.viewList(category,buildingId);


        return ResponseEntity.ok(new ApiResponse(true,200,"지도 리스트 페이지 조회 성공",response));
    }

}
