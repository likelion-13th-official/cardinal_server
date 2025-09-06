package com.likelionsg13th.cardinal.goods.controller;

import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.common.dto.resonseDto.PageDto;
import com.likelionsg13th.cardinal.goods.domain.Goods;
import com.likelionsg13th.cardinal.goods.dto.GoodsDetailResponse;
import com.likelionsg13th.cardinal.goods.dto.GoodsResponse;
import com.likelionsg13th.cardinal.goods.service.GoodsService;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {
    private final GoodsService goodsService;
    private final UserService userService;
    /* 검색 */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchGoods(
            @RequestParam("query") String query,
            @RequestParam("page") int page,
            @AuthenticationPrincipal UserDetails user
    ){
        Long userId= userService.resolveUserIdOrNull(user);
        PageDto<GoodsResponse> response=goodsService.searchGoods(query,page,userId);
        return ResponseEntity.ok(new ApiResponse(true,200,"굿즈 검색 목록 조회 성공",response));
    }

    /*전체 조회*/
    @GetMapping
    public ResponseEntity<ApiResponse> getGoodsList(
            @RequestParam("page") Integer page,
            @AuthenticationPrincipal UserDetails user
    ){
        Long userId= userService.resolveUserIdOrNull(user);
        PageDto<GoodsResponse> response = goodsService.getGoodsList(page, userId);
        return ResponseEntity.ok(new ApiResponse(true, 200, "굿즈 전체 조회 성공", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getGoods(
            @PathVariable long id,
            @AuthenticationPrincipal UserDetails user
        ){
        Long userId= userService.resolveUserIdOrNull(user);
        GoodsDetailResponse response=goodsService.getGoods(id, userId);
        return ResponseEntity.ok(new ApiResponse(true, 200, "굿즈 개별 조회 성공", response));
        
    }

}
