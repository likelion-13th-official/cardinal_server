package com.likelionsg13th.cardinal.performance.exception;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.BusinessException;

public class PerformanceNotFound extends BusinessException {
    public PerformanceNotFound(ErrorCode errorCode) {
        super(errorCode);
    }
}
