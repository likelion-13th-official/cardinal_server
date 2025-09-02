package com.likelionsg13th.cardinal.performance.service;

import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.common.enums.PerformanceCategory;
import com.likelionsg13th.cardinal.common.provider.PerformanceProvider;
import com.likelionsg13th.cardinal.performance.domain.Performance;
import com.likelionsg13th.cardinal.performance.dto.PerformanceResponse;
import com.likelionsg13th.cardinal.performance.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final PerformanceProvider performanceProvider;

    //전체 조회
    public List<PerformanceResponse> getPerfromanceList(PerformanceCategory category, DayOfWeek day, Long userId) {
        List<Performance> performanceList =performanceRepository.findByCategory(category)
                .stream().filter(p -> day==null ||
                        (p.getOperatingDays() !=null && p.getOperatingDays().contains(day))).toList();

        Set<Long> scrappedIds;
        if (userId != null && !performanceList.isEmpty()) {
            List<Long> ids = performanceList.stream().map(Performance::getId).toList();
            scrappedIds = performanceProvider.getScrappedContentIds(ids, userId);
        } else {
            scrappedIds = Set.of();
        }


        List<PerformanceResponse> performanceResponseList = performanceList.stream()
                .map(p -> PerformanceResponse.from(p, scrappedIds.contains(p.getId())))
                .toList();

        return performanceResponseList;
    }

}
