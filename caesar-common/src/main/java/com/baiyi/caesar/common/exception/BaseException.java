package com.baiyi.caesar.common.exception;

import com.baiyi.caesar.domain.ErrorEnum;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/4/19 12:17 下午
 * @Version 1.0
 */
@Data
public abstract class BaseException extends RuntimeException {

    private Integer code;

    public BaseException(String message) {
        super(message);
        this.code = 10000;
    }

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }
}
