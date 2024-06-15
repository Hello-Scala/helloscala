package com.helloscala.web.handle;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.exception.BusinessException;
import com.helloscala.common.web.exception.AbstractException;
import com.helloscala.common.web.exception.GenericException;
import com.helloscala.common.web.response.ErrorResponse;
import com.helloscala.common.web.response.ResponseHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.helloscala.common.ResultCode.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse<GenericException>> handleOtherException(Throwable throwable) {
        ErrorResponse<GenericException> error = ResponseHelper.error(new GenericException(throwable.getMessage(), throwable));
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse<GenericException>> handleOtherException(Exception e) {
        ErrorResponse<GenericException> error = ResponseHelper.error(new GenericException(e.getMessage(), e));
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(AbstractException.class)
    public <E extends AbstractException> ResponseEntity<ErrorResponse<E>> handleException(E e) {
        ErrorResponse<E> error = ResponseHelper.error(e);
        return ResponseEntity.status(e.getStatus()).body(error);
    }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse<GenericException>> BusinessExceptionHandler(BusinessException ex) {
        if (ex.getCode() != -1) {
            log.error("code : " + ex.getCode() + " msg : " + ex.getMessage(), ex);
        }
        if (StringUtils.isBlank(ex.getMessage())) {
            ErrorResponse<GenericException> error = ResponseHelper.error(new GenericException(ex.getMessage(), ex));
            return ResponseEntity.status(error.getStatus()).body(error);
        } else if (StringUtils.isBlank(ex.getLocalizedMessage())) {
            ErrorResponse<GenericException> error = ResponseHelper.error(new GenericException(ex.getLocalizedMessage(), ex));
            return ResponseEntity.status(error.getStatus()).body(error);
        } else {
            ErrorResponse<GenericException> error = ResponseHelper.error(new GenericException(ex));
            return ResponseEntity.status(error.getStatus()).body(error);
        }
    }

    // Assert业务异常
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse<GenericException>> AssertExceptionHandler(IllegalArgumentException ex) {
        log.error(" msg : " + ex.getMessage(), ex);
        if (StringUtils.isBlank(ex.getLocalizedMessage())) {
            ErrorResponse<GenericException> error = ResponseHelper.error(new GenericException(ex.getLocalizedMessage(), ex));
            return ResponseEntity.status(error.getStatus()).body(error);
        } else {
            ErrorResponse<GenericException> error = ResponseHelper.error(new GenericException(ex));
            return ResponseEntity.status(error.getStatus()).body(error);
        }
    }

    @ExceptionHandler(NotLoginException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse<GenericException>> NotLoginExceptionHandler(NotLoginException ex) {
        log.error(" msg : " + ex.getMessage(), ex);
        ErrorResponse<GenericException> error = ResponseHelper.error(new GenericException(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), ex));
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(NotPermissionException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse<GenericException>> NotPermissionExceptionHandler(NotPermissionException ex) {
        log.error(" msg : " + ex.getMessage(), ex);
        ErrorResponse<GenericException> error = ResponseHelper.error(new GenericException(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), ex));
        return ResponseEntity.status(error.getStatus()).body(error);
    }
}