package com.helloscala.common.web.exception;

import cn.hutool.core.util.StrUtil;
import org.springframework.http.HttpStatus;

/**
 * @author Steve Zou
 */
public class ForbiddenException extends AbstractException {
    private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;
    private static final String CODE = "FORBIDDEN";
    private static final String MESSAGE = "Forbidden!";

    public ForbiddenException(String msg, Throwable throwable) {
        super(STATUS, CODE, msg, throwable);
    }


    public ForbiddenException(Throwable throwable) {
        super(STATUS, CODE, MESSAGE, throwable);
    }
    public ForbiddenException(String msgFmt, Object... params) {
        super(STATUS, CODE, StrUtil.format(msgFmt, params));
    }

    public ForbiddenException(String msgFmt, Throwable throwable, Object... params) {
        super(STATUS, CODE, StrUtil.format(msgFmt, params), throwable);
    }
}
