package com.likelionsg13th.cardinal.users.exception;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.BusinessException;

public class ScrapNotFoundException extends BusinessException {
    public ScrapNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
