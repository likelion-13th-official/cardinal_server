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
    BOOTH_NOT_FOUND(HttpStatus.NOT_FOUND,"B001","해당 id의 부스를 찾을 수 없습니다"),

    //공연
    PERFORMANCE_NOT_FOUND(HttpStatus.NOT_FOUND,"P001","해당 id의 공연을 찾을 수 없습니다."),

    //이벤트
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND,"E001","해당 id의 이벤트를 찾을 수 없습니다."),
    //굿즈
    GOODS_NOT_FOUND(HttpStatus.NOT_FOUND,"G001","해당 id의 굿즈를 찾을 수 없습니다."),
    //푸드트런
    LOCATION_NOT_PROVIDED_FOR_FOOD_TRUCK(HttpStatus.BAD_REQUEST, "F001", "푸드트럭 카테고리 조회 시 locationId 파라미터가 필수입니다."),

    //scrap
    SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND,"S001","해당 Id의 스크랩을 찾을 수 없습니다."),
    SCRAP_ALREADY_EXISTS(HttpStatus.CONFLICT,"S002","이미 스크랩한 항목입니다."),
    SCRAP_NOT_SUPPORTED_FOR_CATEGORY(HttpStatus.BAD_REQUEST,"S003","해당 카테고리를 스크랩 할 수 없습니다."),

    //유저
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"U001", "해당 사용자를 찾을 수 없습니다"),
    STAMP_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "U002", "이미 적립한 스탬프입니다"),

    //map
    KEYWORD_NOT_VALID(HttpStatus.BAD_REQUEST,"M001","검색 키워드는 2글자 이상이어야 합니다."),
    //pubadmin
    UPDATE_NOT_ALLOWED(HttpStatus.FORBIDDEN,"A001","해당 정보를 수정할 권한이 없습니다.");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
