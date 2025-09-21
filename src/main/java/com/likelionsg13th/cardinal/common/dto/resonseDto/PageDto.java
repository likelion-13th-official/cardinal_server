package com.likelionsg13th.cardinal.common.dto.resonseDto;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;


/*
* 페이지네이션 메타 데이터
* */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class PageDto<T> implements Serializable {
    private int pageNumber;         // 현재 페이지
    private int pageSize;            // 페이지 크기
    private int totalPages;      // 전체 페이지 수
    private long totalElements;  // 전체 건수
//    private boolean hasNext;     // 다음 페이지 존재 여부
//    private boolean hasPrevious;
    private List<T> contents;



    public static <T> PageDto<T> from (Page<T> page){
        return PageDto.<T>builder()
                .pageNumber(page.getNumber()+1)
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
//                .hasNext(page.hasNext())
//                .hasPrevious(page.hasPrevious())
                .contents(page.getContent())
                .build();
    }

    // goods 에서 page값이 없는 경우 반환
    public static <T> PageDto<T> of(List<T> list) {
        return PageDto.<T>builder()
                .pageNumber(1) // 전체 반환이므로 페이지 번호는 1로 고정
                .pageSize(list.size()) // 전체 사이즈
                .totalPages(1) // 전체 페이지는 1
                .totalElements(list.size()) // 전체 건수
                .contents(list)
                .build();
    }


}
