package com.likelionsg13th.cardinal.common.exception;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;

public class GoodsNotFound extends BusinessException {
    public GoodsNotFound(ErrorCode errorCode) {
        super(errorCode);
    }
}
