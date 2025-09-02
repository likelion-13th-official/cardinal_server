package com.likelionsg13th.cardinal.common.exception;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;

public class ParameterIsNullOrEmpty extends BusinessException {
    public ParameterIsNullOrEmpty(ErrorCode errorCode) {
        super(errorCode);
    }
}
