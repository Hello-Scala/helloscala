package com.helloscala.common.web.exception;

import cn.hutool.core.util.StrUtil;
import org.springframework.http.HttpStatus;

/**
 * @author Steve Zou
 */
public class GenericException extends AbstractException {
    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final String CODE = "INTERNAL_SERVER_ERROR";
    private static final String MESSAGE = "Internal Error!";

    public GenericException() {
        super(STATUS, CODE, MESSAGE);
    }

    public GenericException(HttpStatus status, String code, String msg, Throwable throwable) {
        super(status, code, msg, throwable);
    }

    public GenericException(HttpStatus status, String msg, Throwable throwable) {
        super(status, CODE, msg, throwable);
    }

    public GenericException(String code, String msg, Throwable throwable) {
        super(STATUS, code, msg, throwable);
    }


    public GenericException(String msg, Throwable throwable) {
        super(STATUS, CODE, msg, throwable);
    }


    public GenericException(Throwable throwable) {
        super(STATUS, CODE, MESSAGE, throwable);
    }
    public GenericException(String msgFmt, Object... params) {
        super(STATUS, CODE, StrUtil.format(msgFmt, params));
    }
}

