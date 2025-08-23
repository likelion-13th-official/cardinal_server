package com.likelionsg13th.cardinal.common.exception;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;

public class InvalidCategoryException extends BusinessException{
    public InvalidCategoryException(ErrorCode errodCode) {
        super(errodCode);
    }
}
