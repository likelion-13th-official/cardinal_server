package com.likelionsg13th.cardinal.common.dto.resonseDto;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;


/*
* 페이지네이션 메타 데이터
* */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageDto<T> {
    private int page;            // 현재 페이지
    private int size;            // 페이지 크기
    private int totalPages;      // 전체 페이지 수
    private long totalElements;  // 전체 건수
    private boolean hasNext;     // 다음 페이지 존재 여부
    private boolean hasPrevious;
    private List<T> items;



    public static <T> PageDto<T> from (Page<T> page){
        return PageDto.<T>builder()
                .page(page.getNumber()+1)
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .items(page.getContent())
                .build();
    }


}
