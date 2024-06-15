package com.helloscala.common.web.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Steve Zou
 */
@Getter
public class Response<T> extends AbstractResponse {
    private final T data;

    public Response(T data) {
        super(HttpStatus.OK);
        this.data = data;
    }

    public Response(HttpStatus status, T data) {
        super(status);
        this.data = data;
    }
}
