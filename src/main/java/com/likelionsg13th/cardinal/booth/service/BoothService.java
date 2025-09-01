package com.likelionsg13th.cardinal.booth.service;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.dto.BoothDetailResponse;
import com.likelionsg13th.cardinal.booth.dto.BoothResponse;
import com.likelionsg13th.cardinal.booth.exception.BoothNotFoundException;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.common.dto.resonseDto.PageDto;
import com.likelionsg13th.cardinal.common.enums.BoothCategory;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.InvalidCategoryException;
import com.likelionsg13th.cardinal.users.domain.Users;
import com.likelionsg13th.cardinal.users.dto.UserDto;
import com.likelionsg13th.cardinal.users.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.likelionsg13th.cardinal.common.enums.ContentType.BOOTH;

@Service
@RequiredArgsConstructor
public class BoothService {
    private final BoothRepository boothRepository;
    private final ScrapService scrapService;
    private static final int PAGE_SIZE = 2;

    //전체 목록 조회
    public List<BoothResponse> getBoothList(UserDto userDto, String categoryStr, Boolean isOperating, String dayStr) {

        //요일 변환
        DayOfWeek day = (dayStr != null) ? DayOfWeek.valueOf(dayStr.toUpperCase()) : null;

        List<Booth> booths;
        // 카테고리=ALL인지 확인
        if ("ALL".equalsIgnoreCase(categoryStr)) {
            booths = boothRepository.findAll();
        } else {
            try {
                // enum으로 변환 후 조회
                BoothCategory category = BoothCategory.valueOf(categoryStr.toUpperCase());
                booths = boothRepository.findByCategory(category);
            } catch (IllegalArgumentException e) {
                throw new InvalidCategoryException(ErrorCode.INVALID_CATEGORY);
            }
        }


        return booths.stream()
                // 운영 여부 필터링
                .filter(booth -> isOperating == null || booth.getOperatingInfo().isOperating() == isOperating)
                // 요일 필터링
                .filter(booth -> day == null || booth.getOperatingDays().contains(day))
                // DTO 변환
                .map(booth -> {
                    boolean isScrapped = userDto != null && scrapService.isScrappedByUser(userDto.getId(), BOOTH, booth.getId());
                    return BoothResponse.from(booth, isScrapped);
                })
                .collect(Collectors.toList());
    }

    //개별 상세 조회
    public BoothDetailResponse getBoothDetail(long id) {
        Booth booth=boothRepository.findById(id)
                .orElseThrow(()->new BoothNotFoundException(ErrorCode.BOOTH_NOT_FOUND));
        return BoothDetailResponse.of(booth);
    }



    // 검색
    @Transactional(readOnly = true)
    public PageDto<BoothResponse> searchBooths(UserDto userDto,String query, int page) {
        Pageable pageable= PageRequest.of(page-1,PAGE_SIZE);
        Page<Booth> boothsPage=boothRepository.findByNameOrMenuNameContaining(query,pageable);

        Page<BoothResponse> boothResponsePage=boothsPage.map(
                booth -> {
                    boolean isScrapped = userDto != null && scrapService.isScrappedByUser(userDto.getId(), BOOTH, booth.getId());
                    return BoothResponse.from(booth, isScrapped);
                }
        );

        return PageDto.from(boothResponsePage);
    }
}
