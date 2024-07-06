package com.helloscala.common.web.response;

import com.helloscala.common.web.exception.AbstractException;
import org.springframework.http.HttpStatus;

/**
 * @author Steve Zou
 */
public final class ResponseHelper {
    public static <T> Response<T> ok(T t) {
        return new Response<>(t);
    }

    public static <T> Response<T> ok(HttpStatus status, T t) {
        return new Response<>(status, t);
    }

    public static EmptyResponse ok() {
        return new EmptyResponse();
    }

    public static EmptyResponse ok(HttpStatus status) {
        return new EmptyResponse(status);
    }

    public static <E extends AbstractException> ErrorResponse<E> error(E e) {
        return new ErrorResponse<>(e);
    }
}
