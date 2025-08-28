package com.likelionsg13th.cardinal.common.exception;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;

public class EventNotFound extends BusinessException {
    public EventNotFound(ErrorCode errorCode) {
        super(errorCode);
    }
}
