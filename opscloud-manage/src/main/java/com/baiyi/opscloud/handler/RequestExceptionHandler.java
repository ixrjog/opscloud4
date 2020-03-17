package com.baiyi.opscloud.handler;


import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class RequestExceptionHandler {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HttpResult handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        HttpResult httpResult = new HttpResult(ErrorEnum.SYSTEM_ERROR.getCode(), exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return httpResult;
    }
}
