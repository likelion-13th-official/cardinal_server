package com.likelionsg13th.cardinal.performance.service;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.dto.BoothDetailResponse;
import com.likelionsg13th.cardinal.booth.exception.BoothNotFoundException;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.common.enums.PerformanceCategory;
import com.likelionsg13th.cardinal.performance.domain.Performance;
import com.likelionsg13th.cardinal.performance.dto.PerformanceResponse;
import com.likelionsg13th.cardinal.performance.repository.PerformanceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;

    //전체 조회
    public List<PerformanceResponse> getPerfromanceList(PerformanceCategory category, DayOfWeek day) {
        List<Performance> performanceList =performanceRepository.findByCategory(category);

        List<PerformanceResponse> performanceResponseList = performanceList.stream()
                .filter(p -> day==null ||
                        (p.getOperatingDays() !=null && p.getOperatingDays().contains(day)))
                .map(performance -> PerformanceResponse.from(performance))
                .toList();

        return performanceResponseList;
    }

}
