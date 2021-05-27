package com.baiyi.caesar.common.exception.common;

import com.baiyi.caesar.common.exception.BaseException;
import com.baiyi.caesar.domain.ErrorEnum;

/**
 * @Author baiyi
 * @Date 2021/5/19 2:59 下午
 * @Version 1.0
 */
public class CommonRuntimeException extends BaseException {

    public CommonRuntimeException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

}
