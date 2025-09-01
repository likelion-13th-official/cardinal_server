package com.likelionsg13th.cardinal.goods.exception;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.BusinessException;

public class GoodsNotFound extends BusinessException {
    public GoodsNotFound(ErrorCode errorCode) {
        super(errorCode);
    }
}
