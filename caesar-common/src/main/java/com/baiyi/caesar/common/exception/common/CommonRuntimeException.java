package com.baiyi.caesar.common.exception.common;

import com.baiyi.caesar.common.exception.BaseException;
import com.baiyi.caesar.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2021/5/19 2:59 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommonRuntimeException extends BaseException {

    private Integer code;

    public CommonRuntimeException(String message) {
        super(message);
        this.code = 999;
    }

    public CommonRuntimeException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

}
