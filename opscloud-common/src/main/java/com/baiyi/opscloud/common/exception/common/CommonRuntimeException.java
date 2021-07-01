package com.baiyi.opscloud.common.exception.common;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2021/5/19 2:59 下午
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommonRuntimeException extends BaseException {

    private final Integer code = 999;

    public CommonRuntimeException(String message) {
        super(message);
        setCode(code);
    }

    public CommonRuntimeException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public CommonRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
