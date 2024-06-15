package com.helloscala.common.web.response;

import com.helloscala.common.web.exception.AbstractException;
import lombok.Getter;

/**
 * @author Steve Zou
 */
@Getter
public final class ErrorResponse<E extends AbstractException> extends AbstractResponse {
    private final String code;
    private final String msg;

    public ErrorResponse(E e) {
        super(e.getStatus());
        code = e.getCode();
        msg = e.getMessage();
    }
}
