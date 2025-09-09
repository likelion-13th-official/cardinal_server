package com.likelionsg13th.cardinal.pubOffice.exception;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.BusinessException;

public class UpdateNotAllowed extends BusinessException {
    public UpdateNotAllowed(ErrorCode errorCode) {
        super(errorCode);
    }
}
