package com.likelionsg13th.cardinal.users.controller;

import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.dto.request.StampCreateRequest;
import com.likelionsg13th.cardinal.users.dto.response.StampResponse;
import com.likelionsg13th.cardinal.users.service.StampService;
import com.likelionsg13th.cardinal.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stamps")
public class StampController {
    private final StampService stampService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse> createStamp(
            @RequestBody StampCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        UserDto userDto=userService.getMeBySubject(userDetails.getUsername());
        StampResponse response = stampService.createStamp(userDto,request.getActivityType());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(true,201,"스탬프 생성 성공", response));
    }



}
