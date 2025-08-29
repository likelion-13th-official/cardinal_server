package com.likelionsg13th.cardinal.users.exception;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.BusinessException;

public class StampDuplicateException extends BusinessException {
    public StampDuplicateException(ErrorCode errodCode) {
        super(errodCode);
    }
}
