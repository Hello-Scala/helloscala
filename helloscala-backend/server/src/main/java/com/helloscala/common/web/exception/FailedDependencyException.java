package com.helloscala.common.web.exception;

import cn.hutool.core.util.StrUtil;
import org.springframework.http.HttpStatus;

/**
 * @author Steve Zou
 */
public class FailedDependencyException extends AbstractException {
    private static final HttpStatus STATUS = HttpStatus.FAILED_DEPENDENCY;
    private static final String CODE = "FAILED_DEPENDENCY";
    private static final String MESSAGE = "Failed dependency!";

    public FailedDependencyException(String msg, Throwable throwable) {
        super(STATUS, CODE, msg, throwable);
    }


    public FailedDependencyException(Throwable throwable) {
        super(STATUS, CODE, MESSAGE, throwable);
    }
    public FailedDependencyException(String msgFmt, Object... params) {
        super(STATUS, CODE, StrUtil.format(msgFmt, params));
    }

    public FailedDependencyException(String msgFmt, Throwable throwable, Object... params) {
        super(STATUS, CODE, StrUtil.format(msgFmt, params), throwable);
    }
}
