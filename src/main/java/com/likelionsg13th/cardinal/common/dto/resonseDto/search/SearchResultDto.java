package com.likelionsg13th.cardinal.common.dto.resonseDto.search;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter @Builder
public class SearchResultDto {
    private String contentsName;
    private int totalCount;
    private List<?> items;

    public static <T> SearchResultDto from(String categoryName, Page<T> page) {
        return SearchResultDto.builder()
                .contentsName(categoryName)
                .totalCount((int) page.getTotalElements())
                .items(page.getContent())
                .build();
    }

}
