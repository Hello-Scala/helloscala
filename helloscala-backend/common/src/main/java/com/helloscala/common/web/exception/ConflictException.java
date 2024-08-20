package com.helloscala.common.web.exception;

import cn.hutool.core.util.StrUtil;
import org.springframework.http.HttpStatus;

/**
 * @author Steve Zou
 */
public class ConflictException extends AbstractException {
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;
    private static final String CODE = "CONFLICT";
    private static final String MESSAGE = "Conflict!";

    public ConflictException(String msg, Throwable throwable) {
        super(STATUS, CODE, msg, throwable);
    }


    public ConflictException(Throwable throwable) {
        super(STATUS, CODE, MESSAGE, throwable);
    }
    public ConflictException(String msgFmt, Object... params) {
        super(STATUS, CODE, StrUtil.format(msgFmt, params));
    }

    public ConflictException(String msgFmt, Throwable throwable, Object... params) {
        super(STATUS, CODE, StrUtil.format(msgFmt, params), throwable);
    }
}
