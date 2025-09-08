package com.likelionsg13th.cardinal.common.dto.resonseDto.search;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class AutoCompleteDto {

    private long id;
    private String name;
    private String contentsType;
}
