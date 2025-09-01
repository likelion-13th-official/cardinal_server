package com.likelionsg13th.cardinal.event.exception;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.BusinessException;

public class EventNotFound extends BusinessException {
    public EventNotFound(ErrorCode errorCode) {
        super(errorCode);
    }
}
