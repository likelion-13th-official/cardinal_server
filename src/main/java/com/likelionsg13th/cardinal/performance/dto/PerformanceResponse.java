package com.likelionsg13th.cardinal.performance.dto;

import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import com.likelionsg13th.cardinal.performance.domain.Performance;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class PerformanceResponse {
    private Long id;
    private String category;
    private String name;
    private String description;
    private String thumbnail;

    private OperatingInfo operatingInfo;
    private List<String> operatingDays;

    private boolean isScrapped;

    public static PerformanceResponse from(Performance performance) {
        return PerformanceResponse.builder()
                .id(performance.getId())
                .name(performance.getName())
                .category(performance.getCategory() == null ? null: performance.getCategory().toKorean())
                .description(performance.getDescription())
                //TO-DO 북마크
                .thumbnail(performance.getThumbnailUrl())
                .operatingDays(
                        performance.getOperatingDays().stream()
                                .map(day -> day.toKorean())
                                .collect(Collectors.toList())
                )
                .build();

    }
}
