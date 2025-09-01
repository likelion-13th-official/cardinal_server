package com.likelionsg13th.cardinal.users.exception;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.BusinessException;

public class ScrapNotSupportedForCategory extends BusinessException {
    public ScrapNotSupportedForCategory(ErrorCode errorCode) {
        super(errorCode);
    }
}
