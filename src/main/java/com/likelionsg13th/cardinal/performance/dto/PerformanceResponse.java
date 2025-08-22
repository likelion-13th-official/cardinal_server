package com.likelionsg13th.cardinal.performance.dto;

import com.likelionsg13th.cardinal.performance.domain.Performance;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PerformanceResponse {
    private Long id;
    private String category;
    private String name;
    private LocalDateTime date;
    private String description;
    private String thumbnail;

    public static PerformanceResponse from(Performance performance) {
        return PerformanceResponse.builder()
                .id(performance.getId())
                .category(performance.getCategory() == null ? null: performance.getCategory().toKorean())
                .date(performance.getDate())
                .description(performance.getDescription())
                .thumbnail(performance.getThumbnailUrl())
                .build();

    }
}
