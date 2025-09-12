package com.likelionsg13th.cardinal.common.dto.resonseDto.search;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Builder
public class SearchTrendDto {
    private LocalDateTime updatedAt;
    private List<SimpleSearchDto> trending;


    public static SearchTrendDto from(LocalDateTime lastCacheUpdateTime, List<SimpleSearchDto> trending) {
        return SearchTrendDto.builder()
                .updatedAt(lastCacheUpdateTime)
                .trending(trending)
                .build();
    }
}
