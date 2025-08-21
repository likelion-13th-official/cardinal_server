package com.likelionsg13th.cardinal.booth.service;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.dto.BoothResponse;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.common.enums.BoothCategory;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import lombok.RequiredArgsConstructor;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoothService {
    private final BoothRepository boothRepository;

    //전체 목록 조회
    public List<BoothResponse> getBoothList(String categoryStr, Boolean isOperating, String dayStr) {
        //요일 변환
        DayOfWeek day = (dayStr != null) ? DayOfWeek.valueOf(dayStr.toUpperCase()) : null;

        List<Booth> booths;
        // 카테고리=ALL인지 확인
        if ("ALL".equalsIgnoreCase(categoryStr)) {
            booths = boothRepository.findAll();
        } else {
            // enum으로 변환 후 조회
            BoothCategory category = BoothCategory.valueOf(categoryStr.toUpperCase());
            booths = boothRepository.findByCategory(category);
        }

        return booths.stream()
                // 운영 여부 필터링
                .filter(booth -> isOperating == null || booth.getOperatingInfo().isOperating() == isOperating)
                // 요일 필터링
                .filter(booth -> day == null || booth.getOperatingDays().contains(day))
                // DTO 변환
                .map(BoothResponse::of)
                .collect(Collectors.toList());
    }

}
