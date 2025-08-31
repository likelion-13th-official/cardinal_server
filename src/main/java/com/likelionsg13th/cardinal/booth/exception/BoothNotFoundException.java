package com.likelionsg13th.cardinal.booth.exception;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.BusinessException;

public class BoothNotFoundException extends BusinessException {
    public BoothNotFoundException(ErrorCode errodCode) {
        super(errodCode);
    }
}
