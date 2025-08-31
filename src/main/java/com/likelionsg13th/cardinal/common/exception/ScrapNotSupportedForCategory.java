package com.likelionsg13th.cardinal.common.exception;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;

public class ScrapNotSupportedForCategory extends BusinessException {
    public ScrapNotSupportedForCategory(ErrorCode errorCode) {
        super(errorCode);
    }
}
