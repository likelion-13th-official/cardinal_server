package com.likelionsg13th.cardinal.common.controller;

import com.likelionsg13th.cardinal.common.service.IndexingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/indexing")
@RequiredArgsConstructor
public class IndexingController {

    private final IndexingService indexingService;

    @PostMapping("/booths")
    public ResponseEntity<String> indexAllBooths() {
        try {
            indexingService.indexAllBooths();
            return ResponseEntity.ok("Booth 데이터 전체 색인 작업이 성공적으로 시작되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("색인 작업 중 오류 발생: " + e.getMessage());
        }
    }

    @PostMapping("/events")
    public ResponseEntity<String> indexAllEvents() {
        try {
            indexingService.indexAllEvents();
            return ResponseEntity.ok("Event 데이터 전체 색인 작업이 성공적으로 시작되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("색인 작업 중 오류 발생: " + e.getMessage());
        }
    }

    @PostMapping("/goods")
    public ResponseEntity<String> indexAllGoods() {
        try {
            indexingService.indexAllGoods();
            return ResponseEntity.ok("Goods 데이터 전체 색인 작업이 성공적으로 시작되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("색인 작업 중 오류 발생: " + e.getMessage());
        }
    }

}