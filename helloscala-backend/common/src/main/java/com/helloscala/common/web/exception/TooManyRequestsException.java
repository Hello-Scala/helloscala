package com.helloscala.common.web.exception;

import cn.hutool.core.util.StrUtil;
import org.springframework.http.HttpStatus;

/**
 * @author Steve Zou
 */
public class TooManyRequestsException  extends AbstractException {
    private static final HttpStatus STATUS = HttpStatus.TOO_MANY_REQUESTS;
    private static final String CODE = "TOO_MANY_REQUESTS";
    private static final String MESSAGE = "Too many requests!";

    public TooManyRequestsException(String msg, Throwable throwable) {
        super(STATUS, CODE, msg, throwable);
    }


    public TooManyRequestsException(Throwable throwable) {
        super(STATUS, CODE, MESSAGE, throwable);
    }
    public TooManyRequestsException(String msgFmt, Object... params) {
        super(STATUS, CODE, StrUtil.format(msgFmt, params));
    }

    public TooManyRequestsException(String msgFmt, Throwable throwable, Object... params) {
        super(STATUS, CODE, StrUtil.format(msgFmt, params), throwable);
    }
}
