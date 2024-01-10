package com.baiyi.opscloud.common.exception.datasource;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2021/5/17 2:01 下午
 * @Version 1.0
 */
public class DatasourceException extends BaseException {

    @Serial
    private static final long serialVersionUID = 6568038823824057120L;

    public DatasourceException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

}