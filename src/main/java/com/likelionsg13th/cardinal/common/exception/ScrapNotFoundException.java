package com.likelionsg13th.cardinal.common.exception;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;

public class ScrapNotFoundException extends BusinessException {
    public ScrapNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
