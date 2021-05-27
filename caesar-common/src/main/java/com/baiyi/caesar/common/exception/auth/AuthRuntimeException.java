package com.baiyi.caesar.common.exception.auth;

import com.baiyi.caesar.common.exception.BaseException;
import com.baiyi.caesar.domain.ErrorEnum;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/5/13 4:05 下午
 * @Version 1.0
 */
@Data
public class AuthRuntimeException extends BaseException {

    private Integer code;

    public AuthRuntimeException(String message) {
        super(message);
        this.code = 999;
    }

    public AuthRuntimeException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public AuthRuntimeException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }
}

