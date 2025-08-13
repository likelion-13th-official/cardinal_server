package com.likelionsg13th.cardinal.pub.controller;

import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.pub.dto.resonseDto.PubsResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pubs")
public class PubController {


    //isoperating={true}&day={mon}
    @GetMapping
    public ResponseEntity<ApiResponse> viewPubs(@RequestParam boolean isOperating,@RequestParam String day){

        List<PubsResponseDto> response = new ArrayList<>();
        return ResponseEntity.ok(new ApiResponse(true, 200,"주점 리스트 조회 성공.",response));

    }

}
