package com.likelionsg13th.cardinal.users.controller;

import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.repository.ScrapRepository;
import com.likelionsg13th.cardinal.users.service.ScrapService;
import com.likelionsg13th.cardinal.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scraps")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;
    private final UserService userService;
    @GetMapping()
    public ResponseEntity<ApiResponse> viewScraps(@AuthenticationPrincipal UserDetails principal, //user
                                                  @RequestParam(required = false) String day,
                                                  @RequestParam(required = false) String category) {

        return ResponseEntity.ok(new ApiResponse(true, 200, "스크랩 조회 성공"));
    }
     @PostMapping()
    public ResponseEntity<ApiResponse> createScraps(@AuthenticationPrincipal UserDetails principal,String category,String categoryId) {
        UserDto user = userService.getMeBySubject(principal.getUsername());
        scrapService.addScraps(user,category,categoryId);
        return ResponseEntity.ok(new ApiResponse(true, 200, "스크랩 생성 성공"));
    }
     @DeleteMapping()
    public ResponseEntity<ApiResponse> DeleteAllScraps(@AuthenticationPrincipal UserDetails principal) {
        UserDto user = userService.getMeBySubject(principal.getUsername());
        scrapService.DeleteAllScrapByUserId(user);
        return ResponseEntity.ok(new ApiResponse(true, 200, "스크랩 전체 삭제 성공",null));
    }

    @DeleteMapping("/{scrapId}")
    public ResponseEntity<ApiResponse> DeleteScrapsById(@AuthenticationPrincipal UserDetails principal,@PathVariable Long scrapId) {
        UserDto user = userService.getMeBySubject(principal.getUsername());
        scrapService.deleteScrapByScrapId(user, scrapId);
        return ResponseEntity.ok(new ApiResponse(true, 200, "스크랩 삭제 성공",null));
    }


}
