package com.baiyi.opscloud.handler;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@SuppressWarnings("rawtypes")
@Slf4j
@RestControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(value = {BaseException.class})
    public HttpResult handleRuntimeException(BaseException exception) {
        return new HttpResult(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(value = {EncryptionOperationNotPossibleException.class})
    public HttpResult handleRuntimeException(EncryptionOperationNotPossibleException exception) {
        return new HttpResult(999, "加密/解密错误");
    }

}
