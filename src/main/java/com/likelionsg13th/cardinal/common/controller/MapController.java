package com.likelionsg13th.cardinal.common.controller;

import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapDetailDto;
import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapSearchDto;
import com.likelionsg13th.cardinal.common.dto.resonseDto.map.MapListDto;
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

    @GetMapping()
    public ResponseEntity<ApiResponse> getMarkersByCategory(@RequestParam String category){

       Object response =   mapService.getMapMarkersByCategory(category);

        return ResponseEntity.ok(new ApiResponse(true,200,"지도 카테고리 별 페이지 조회 성공",response));
    }

    @GetMapping("/detail")
    public ResponseEntity<ApiResponse> getDetail(@RequestParam String category,
                                                 @RequestParam(required = false) Long id){

        MapDetailDto response = mapService.getDetail(category,id);
        return ResponseEntity.ok(new ApiResponse(true,200,"지도 상세 페이지 조회 성공",response));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getList(@RequestParam String category,
                                               @RequestParam(required = false) Long buildingId){

        MapListDto response = mapService.getList(category,buildingId);
        return ResponseEntity.ok(new ApiResponse(true,200,"지도 리스트 페이지 조회 성공",response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getSearch(@RequestParam String keyword){

        List<MapSearchDto> resopnse =  mapService.getSearchResult(keyword);
        return ResponseEntity.ok(new ApiResponse(true,200,"지도 검색 결과 페이지 조회 성공",resopnse));
    }

}
