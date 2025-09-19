package com.likelionsg13th.cardinal.map.controller;

import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.map.dto.MapDetailDto;
import com.likelionsg13th.cardinal.map.dto.MapListDto;
import com.likelionsg13th.cardinal.map.dto.MapSearchResponseDto;
import com.likelionsg13th.cardinal.map.service.MapService;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.service.UserService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
@Validated
@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;
    private final UserService  userService;

    @GetMapping()
    public ResponseEntity<ApiResponse> getMarkersByCategory(@RequestParam @NotBlank String category){

       Object response =   mapService.getMapMarkersByCategory(category);

        return ResponseEntity.ok(new ApiResponse(true,200,"지도 카테고리 별 페이지 조회 성공",response));
    }

    @GetMapping("/detail")
    public ResponseEntity<ApiResponse> getDetail(@RequestParam @NotBlank String category,
                                                 @RequestParam(required = false) Long id){

        MapDetailDto response = mapService.getDetail(category,id);
        return ResponseEntity.ok(new ApiResponse(true,200,"지도 상세 페이지 조회 성공",response));
    }

    /*pub,foodtruck,yard_project 전용 api */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getList(@RequestParam @NotBlank String category,
                                               @RequestParam(required = false) Long locationId,
                                               @AuthenticationPrincipal UserDetails user ){

        UserDto userDto = null; //
        if (user != null) {
            userDto = userService.getMeBySubject(user.getUsername());
        }

        MapListDto response = mapService.getList(category,locationId,userDto);
        return ResponseEntity.ok(new ApiResponse(true,200,"지도 리스트 페이지 조회 성공",response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getSearch(@RequestParam @NotBlank @Size(min = 2,message = "검색어는 최소 2글자 이상") String keyword,
                                                 @AuthenticationPrincipal UserDetails user){
        UserDto userDto = null; //
        if (user != null) {
            userDto = userService.getMeBySubject(user.getUsername());
        }
        List<MapSearchResponseDto> resopnse =  mapService.getSearchResult(keyword,userDto);
        return ResponseEntity.ok(new ApiResponse(true,200,"지도 검색 결과 페이지 조회 성공",resopnse));
    }

}
