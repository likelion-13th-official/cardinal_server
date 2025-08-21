package com.likelionsg13th.cardinal.common.dto.resonseDto.search;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @Builder
public class SearchResponse {
    private List<ContentsResultDto> contentsResultDto;

    public static SearchResponse from(List<ContentsResultDto> contentsResultDtos){
        return  SearchResponse.builder()
                .contentsResultDto(contentsResultDtos)
                .build();
    }
}
