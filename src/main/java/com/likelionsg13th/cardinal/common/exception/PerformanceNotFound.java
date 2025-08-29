package com.likelionsg13th.cardinal.common.exception;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;

public class PerformanceNotFound extends BusinessException {
    public PerformanceNotFound(ErrorCode errorCode) {
        super(errorCode);
    }
}
