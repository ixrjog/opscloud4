package com.baiyi.opscloud.common.exception.datasource;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;

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

