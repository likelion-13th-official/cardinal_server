package com.likelionsg13th.cardinal.common.controller;

import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.common.dto.resonseDto.search.SearchTrendDto;
import com.likelionsg13th.cardinal.common.dto.resonseDto.search.SimpleSearchDto;
import com.likelionsg13th.cardinal.common.dto.resonseDto.search.SearchResultDto;
import com.likelionsg13th.cardinal.common.service.SearchService;
import com.likelionsg13th.cardinal.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserService userService;

    /* 전체 검색 결과 초기화면*/
    @GetMapping()
    public ResponseEntity<ApiResponse> searchAll(
            @RequestParam("query") String query,
            @AuthenticationPrincipal UserDetails user)
    {

        Long userId= userService.resolveUserIdOrNull(user);
        List<SearchResultDto> response=searchService.searchAll(query, userId);
        return ResponseEntity.ok(new ApiResponse(true,200,"검색결과 초기화면 조회 성공",response));
    }


    /* 자동완성*/
    @GetMapping("/autocomplete")
    public ResponseEntity<ApiResponse> getSuggestions(
            @RequestParam("query") String query
    ){
        List<SimpleSearchDto> response=searchService.getSuggestion(query);
        return ResponseEntity.ok(new ApiResponse(true,200,"추천 검색어 조회 성공", response));
    }

    /*인기검색어*/
    @GetMapping("/trending")
    public ResponseEntity<ApiResponse> getPopularQueries(){
        SearchTrendDto response=searchService.getPopularQueries();
        return ResponseEntity.ok(new ApiResponse(true,200,"인기 검색어 조회 성공", response));
    }


}
