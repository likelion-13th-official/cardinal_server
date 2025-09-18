package com.likelionsg13th.cardinal.event.controller;

import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.common.dto.resonseDto.PageDto;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.event.dto.EventDetailResponse;
import com.likelionsg13th.cardinal.event.dto.EventResponse;
import com.likelionsg13th.cardinal.event.dto.EventSimpleResponse;
import com.likelionsg13th.cardinal.event.service.EventService;
import lombok.RequiredArgsConstructor;
import com.likelionsg13th.cardinal.users.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    private final UserService userService;

    /* 검색*/
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchEvent(
            @RequestParam("query") String query,
            @RequestParam("page") int page,
            @AuthenticationPrincipal UserDetails user
    ) {
        Long userId= userService.resolveUserIdOrNull(user);
        PageDto<EventResponse> response=eventService.searchEvents(query,page,userId);
        return ResponseEntity.ok(new ApiResponse(true,200,"이벤트 검색 목록 조회 성공",response));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getEvent(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal
    ){
        Long userId = userService.resolveUserIdOrNull(principal);
        EventDetailResponse response = eventService.getEvent(id, userId);
        return ResponseEntity.ok(new ApiResponse(true, 200, "이벤트 개별 조회 성공", response));
    }

    @GetMapping("/cal")
    public ResponseEntity<ApiResponse> getEventsCal(
            @RequestParam(value="day", required = false)DayOfWeek day
            ){
        List<EventSimpleResponse> response = eventService.getEventCal(day);
        return ResponseEntity.ok(new ApiResponse(true, 200, "이벤트 전체조회 성공", response));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse> getEvents(
            @RequestParam(value="day", required = false)DayOfWeek day
    ){
        List<EventSimpleResponse> response = eventService.getEventCal(day);
        return ResponseEntity.ok(new ApiResponse(true, 200, "이벤트 전체조회 성공", response));
    }



}
