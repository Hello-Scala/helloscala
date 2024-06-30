package com.helloscala.common.web.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Steve Zou
 */
@Getter
public abstract class AbstractResponse {
    private final HttpStatus status;
    private final int statusCode;

    public AbstractResponse(HttpStatus status) {
        this.status = status;
        this.statusCode = status.value();
    }
}
