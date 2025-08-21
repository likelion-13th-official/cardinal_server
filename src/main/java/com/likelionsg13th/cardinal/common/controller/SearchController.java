package com.likelionsg13th.cardinal.common.controller;

import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.common.dto.resonseDto.search.SearchResultDto;
import com.likelionsg13th.cardinal.common.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;

    /* 전체 검색 결과 초기화면*/
    @GetMapping()
    public ResponseEntity<ApiResponse> searchAll(
            @RequestParam("query") String query
    ){
        List<SearchResultDto> response=searchService.searchAll(query);
        return ResponseEntity.ok(new ApiResponse(true,200,"검색결과 초기화면 조회 성공",response));
    }


}
