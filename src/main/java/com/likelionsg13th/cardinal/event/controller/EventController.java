package com.likelionsg13th.cardinal.event.controller;

import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.common.dto.resonseDto.PageDto;
import com.likelionsg13th.cardinal.event.dto.EventDetailResponse;
import com.likelionsg13th.cardinal.event.dto.EventResponse;
import com.likelionsg13th.cardinal.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    /* 검색*/
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchEvent(
            @RequestParam("query") String query,
            @RequestParam("page") int page
    ) {
        PageDto<EventResponse> response=eventService.searchEvents(query,page);
        return ResponseEntity.ok(new ApiResponse(true,200,"이벤트 검색 목록 조회 성공",response));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getEvent(@PathVariable Long id){
        EventDetailResponse response = eventService.getEvent(id);
        return ResponseEntity.ok(new ApiResponse(true, 200, "이벤트 개별 조회 성공", response));
    }

}
