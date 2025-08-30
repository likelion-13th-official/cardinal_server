package com.likelionsg13th.cardinal.common.dto.resonseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @AllArgsConstructor @Builder
public class ListResponseDto<T> {
    int size;
    List<T> result;

    public static <T> ListResponseDto<T> from(List<T> data) {
        return ListResponseDto.<T>builder()
                .size(data.size())
                .result(data)
                .build();
    }
}
