package com.likelionsg13th.cardinal.performance.service;

import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.common.enums.PerformanceCategory;
import com.likelionsg13th.cardinal.common.provider.PerformanceProvider;
import com.likelionsg13th.cardinal.common.service.UpdateIsOperating;
import com.likelionsg13th.cardinal.performance.domain.Performance;
import com.likelionsg13th.cardinal.performance.dto.PerformanceResponse;
import com.likelionsg13th.cardinal.performance.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final PerformanceProvider performanceProvider;
    private final UpdateIsOperating updateIsOperating;

    //전체 조회
    public List<PerformanceResponse> getPerfromanceList(String category, DayOfWeek day, Long userId) {
/*        List<Performance> performanceList =performanceRepository.findByCategory(category)
                .stream().filter(p -> day==null ||
                        (p.getOperatingDays() !=null && p.getOperatingDays().contains(day))).toList();*/
        // 1) 카테고리 파싱: "ALL"이면 null로 두어 전체 조회
        PerformanceCategory cate = null;
        if (category != null && !"ALL".equalsIgnoreCase(category)) {
            cate = PerformanceCategory.valueOf(category.toUpperCase()); // CLUB / ARTIST
        }

        // 2) DB 조회: 카테고리 유무에 따라 분기
        List<Performance> baseList = (cate == null)
                ? performanceRepository.findAll()
                : performanceRepository.findByCategory(cate);

        // 3) 요일 필터: day == null이면 전체 통과
        List<Performance> performanceList = baseList.stream()
                .filter(p -> day == null ||
                        (p.getOperatingDays() != null && p.getOperatingDays().contains(day)))
                .toList();

        Set<Long> scrappedIds;
        if (userId != null && !performanceList.isEmpty()) {
            List<Long> ids = performanceList.stream().map(Performance::getId).toList();
            scrappedIds = performanceProvider.getScrappedContentIds(ids, userId);
        } else {
            scrappedIds = Set.of();
        }




        List<PerformanceResponse> performanceResponseList = performanceList.stream()
                .map(p -> {
                    OperatingInfo src = p.getOperatingInfo(); // 엔티티의 OI (절대 변경 X)
                    OperatingInfo viewOi = null;

                    if (src != null) {
                        boolean currentIsOperating =
                                updateIsOperating.updateOperatingStatus(src, p.getOperatingDays());

                        // ★ 복제본 생성 (도메인 수정/엔티티 변경 없음)
                        viewOi = new OperatingInfo();
                        viewOi.setStartTime(src.getStartTime());
                        viewOi.setEndTime(src.getEndTime());
                        viewOi.setOperating(currentIsOperating); // 계산값만 세팅
                    }

                    boolean scrapped = scrappedIds.contains(p.getId());
                    return PerformanceResponse.from(p, scrapped, viewOi);
                })
                .toList();

        return performanceResponseList;
    }

}
