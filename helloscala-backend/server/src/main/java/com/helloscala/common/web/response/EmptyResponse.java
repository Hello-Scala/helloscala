package com.helloscala.common.web.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Steve Zou
 */
@Getter
public final class EmptyResponse extends AbstractResponse {
    public static final String OK = "Ok!";
    private final String msg;

    public EmptyResponse() {
        super(HttpStatus.OK);
        msg = OK;
    }

    public EmptyResponse(String msg) {
        super(HttpStatus.OK);
        this.msg = msg;
    }

    public EmptyResponse(HttpStatus status) {
        super(status);
        this.msg = OK;
    }

    public EmptyResponse(HttpStatus status, String msg) {
        super(status);
        this.msg = msg;
    }
}
