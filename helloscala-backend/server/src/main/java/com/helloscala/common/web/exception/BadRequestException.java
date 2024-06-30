package com.helloscala.common.web.exception;

import cn.hutool.core.util.StrUtil;
import org.springframework.http.HttpStatus;

/**
 * @author Steve Zou
 */
public class BadRequestException extends AbstractException {
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String CODE = "BAD_REQUEST";
    private static final String MESSAGE = "Bad request!";

    public BadRequestException(String msg, Throwable throwable) {
        super(STATUS, CODE, msg, throwable);
    }


    public BadRequestException(Throwable throwable) {
        super(STATUS, CODE, MESSAGE, throwable);
    }
    public BadRequestException(String msgFmt, Object... params) {
        super(STATUS, CODE, StrUtil.format(msgFmt, params));
    }

    public BadRequestException(String msgFmt, Throwable throwable, Object... params) {
        super(STATUS, CODE, StrUtil.format(msgFmt, params), throwable);
    }
}
