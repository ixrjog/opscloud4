package com.baiyi.caesar.common.exception.datasource;

import com.baiyi.caesar.common.exception.BaseException;
import com.baiyi.caesar.domain.ErrorEnum;

/**
 * @Author baiyi
 * @Date 2021/5/17 2:01 下午
 * @Version 1.0
 */
public class DatasourceRuntimeException extends BaseException {

    public DatasourceRuntimeException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

}

