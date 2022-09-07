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

    private static final long serialVersionUID = -2938977933351653100L;
    private final Integer code = 999;

    public CommonRuntimeException(String message) {
        super(message);
        setCode(code);
    }

    public CommonRuntimeException(String message, Object... var2) {
        super(message,var2);
        setCode(code);
    }


    public CommonRuntimeException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public CommonRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
