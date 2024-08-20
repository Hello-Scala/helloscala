package com.helloscala.common.web.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Steve Zou
 */
@Getter
public abstract class AbstractException extends RuntimeException {
    private final HttpStatus status;
    private final String code;
    private final String message;

    public AbstractException(HttpStatus status, String code, String message, Throwable throwable) {
        initCause(throwable);
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public AbstractException(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
