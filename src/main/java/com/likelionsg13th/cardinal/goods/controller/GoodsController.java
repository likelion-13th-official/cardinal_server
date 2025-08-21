package com.likelionsg13th.cardinal.goods.controller;

import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.common.dto.resonseDto.PageDto;
import com.likelionsg13th.cardinal.goods.dto.GoodsResponse;
import com.likelionsg13th.cardinal.goods.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {
    private final GoodsService goodsService;

    /* 검색 */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchGoods(
            @RequestParam("query") String query,
            @RequestParam("page") int page
    ){
        PageDto<GoodsResponse> response=goodsService.searchGoods(query,page);
        return ResponseEntity.ok(new ApiResponse(true,200,"굿즈 검색 목록 조회 성공",response));
    }

}
