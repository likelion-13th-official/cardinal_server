package com.likelionsg13th.cardinal.common.exception;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private final ErrorCode errodCode;

    public BusinessException(ErrorCode errodCode) {
        super(errodCode.getMessage());
        this.errodCode = errodCode;
    }

}
