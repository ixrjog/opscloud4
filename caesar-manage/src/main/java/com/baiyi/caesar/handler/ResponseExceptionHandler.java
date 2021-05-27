package com.baiyi.caesar.handler;

import com.baiyi.caesar.common.exception.BaseException;
import com.baiyi.caesar.common.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(value = {BaseException.class})
    public HttpResult handleRuntimeException(BaseException exception) {
        return new HttpResult(exception.getCode(), exception.getMessage());
    }
}
