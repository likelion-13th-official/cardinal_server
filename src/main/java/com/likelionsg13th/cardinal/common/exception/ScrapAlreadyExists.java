package com.likelionsg13th.cardinal.common.exception;

import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import jdk.jshell.JShell;

public class ScrapAlreadyExists extends BusinessException {
    public ScrapAlreadyExists(ErrorCode errorCode) {
        super(errorCode);
    }
}
