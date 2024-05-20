package com.baiyi.opscloud.common.exception.datasource;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2021/5/17 2:01 下午
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DatasourceException extends BaseException {

    @Serial
    private static final long serialVersionUID = 6568038823824057120L;

    private final Integer code = 50000;

    public DatasourceException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public DatasourceException(String message) {
        super(message);
        setCode(code);
    }

}