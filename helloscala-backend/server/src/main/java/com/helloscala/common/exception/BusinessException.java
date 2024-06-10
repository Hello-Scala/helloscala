package com.helloscala.common.exception;

import cn.hutool.core.util.StrUtil;
import com.helloscala.common.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.helloscala.common.ResultCode.ERROR;
import static com.helloscala.common.ResultCode.ERROR_DEFAULT;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {
    protected Integer code;
    protected String message;

    public BusinessException() {
        super();
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getDesc());
        this.code = resultCode.getCode();
        this.message = resultCode.getDesc();
    }

    public BusinessException(String msg) {
        super(msg);
        this.code = ERROR_DEFAULT.getCode();
        this.message = msg;
    }

    public BusinessException(String msgFmt, Object... param) {
        super(StrUtil.format(msgFmt, param));
        this.code = ERROR_DEFAULT.getCode();
        this.message = StrUtil.format(msgFmt, param);
    }

    public BusinessException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.message = msg;
    }

    public BusinessException(Integer code, String msgFmt, Object... param) {
        super(StrUtil.format(msgFmt, param));
        this.code = code;
        this.message = StrUtil.format(msgFmt, param);
    }

    public BusinessException(Integer code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.message = msg;
    }

    public BusinessException(Throwable cause) {
        super(cause);
        this.code = ERROR.getCode();
        this.message = cause.getMessage();
    }

    @Override
    public String toString() {
        return "errorCode: " + code + ", message: " + message;
    }
}
