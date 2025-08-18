package com.likelionsg13th.cardinal.common.controller;

import com.likelionsg13th.cardinal.common.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController {

    private final MapService mapService;

    @GetMapping("/detail")
    public void getDetail(@RequestParam String category,
                          @RequestParam(required = false) Long id){
        

        mapService.viewDetail(category,id);

    }
}
