package com.likelionsg13th.cardinal.users.controller;

import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.dto.request.ScrapRequestDto;
import com.likelionsg13th.cardinal.users.service.ScrapService;
import com.likelionsg13th.cardinal.users.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
    public ResponseEntity<ApiResponse> viewScraps(@AuthenticationPrincipal @Valid UserDetails principal, //user
                                                  @RequestParam(required = false)  String day,
                                                  @RequestParam(required = false) Boolean isOperating) {
        UserDto user = userService.getMeBySubject(principal.getUsername());

        return ResponseEntity.ok(new ApiResponse(true, 200, "스크랩 조회 성공",scrapService.getAllScrapsByDayOrIsOperation(user,day,isOperating)));
    }
     @PostMapping()
    public ResponseEntity<ApiResponse> createScraps(@AuthenticationPrincipal @Valid UserDetails principal,
                                                    @RequestBody @Valid ScrapRequestDto scrapRequestDto) {
        UserDto user = userService.getMeBySubject(principal.getUsername());
        scrapService.addScrap(user,scrapRequestDto);
        return ResponseEntity.ok(new ApiResponse(true, 200, "스크랩 생성 성공"));
    }
     @DeleteMapping()
    public ResponseEntity<ApiResponse> DeleteAllScraps(@AuthenticationPrincipal @Valid UserDetails principal) {
        UserDto user = userService.getMeBySubject(principal.getUsername());
        scrapService.DeleteAllScrapByUserId(user);
        return ResponseEntity.ok(new ApiResponse(true, 200, "스크랩 전체 삭제 성공",null));
    }

    @DeleteMapping("/{scrapId}")
    public ResponseEntity<ApiResponse> DeleteScrapsById(@AuthenticationPrincipal @Valid UserDetails principal
                                                        ,@PathVariable  Long scrapId) {
        UserDto user = userService.getMeBySubject(principal.getUsername());
        scrapService.deleteScrapByScrapId(user, scrapId);
        return ResponseEntity.ok(new ApiResponse(true, 200, "스크랩 삭제 성공",null));
    }


}
