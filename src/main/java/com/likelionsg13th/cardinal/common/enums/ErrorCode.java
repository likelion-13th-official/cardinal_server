package com.likelionsg13th.cardinal.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;

@Getter @RequiredArgsConstructor
public enum ErrorCode {
    //common
    INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "C001", "유효하지 않은 카테고리입니다."),
    PARAMETER_IS_NULL_OR_EMPTY(HttpStatus.BAD_REQUEST,"C002","매개변수가 NULL 또는 비어있습니다."),
    //부스
    BOOTH_NOT_FOUND(HttpStatus.NOT_FOUND,"B001","해당 id의 부스를 찾을 수 없습니다");
    //공연

    //이벤트

    //굿즈



    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
