package com.helloscala.common.web.exception;

import cn.hutool.core.util.StrUtil;
import org.springframework.http.HttpStatus;

/**
 * @author Steve Zou
 */
public class NotFoundException extends AbstractException {
    private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;
    private static final String CODE = "NOT_FOUND";
    private static final String MESSAGE = "Not found!";

    public NotFoundException(String msg, Throwable throwable) {
        super(STATUS, CODE, msg, throwable);
    }


    public NotFoundException(Throwable throwable) {
        super(STATUS, CODE, MESSAGE, throwable);
    }
    public NotFoundException(String msgFmt, Object... params) {
        super(STATUS, CODE, StrUtil.format(msgFmt, params));
    }

    public NotFoundException(String msgFmt, Throwable throwable, Object... params) {
        super(STATUS, CODE, StrUtil.format(msgFmt, params), throwable);
    }
}
