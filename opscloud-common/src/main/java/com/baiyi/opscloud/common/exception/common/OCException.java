package com.baiyi.opscloud.common.exception.common;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2021/5/19 2:59 下午
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OCException extends BaseException {

    @Serial
    private static final long serialVersionUID = -2938977933351653100L;
    private final Integer code = 999;

    public OCException(String message) {
        super(message);
        setCode(code);
    }

    public OCException(String message, Object... var2) {
        super(message,var2);
        setCode(code);
    }

    public OCException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public OCException(String message, Throwable cause) {
        super(message, cause);
    }

}