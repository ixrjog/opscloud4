package com.baiyi.opscloud.core.exception;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2023/12/26 09:38
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DatasourceConfigException extends BaseException {

    @Serial
    private static final long serialVersionUID = 2574941824206512409L;

    private final Integer code = 20001;

    public DatasourceConfigException(String message) {
        super(message);
        setCode(code);
    }

    public DatasourceConfigException(String message, Object... var2) {
        super(message, var2);
        setCode(code);
    }

    public DatasourceConfigException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public DatasourceConfigException(String message, Throwable cause) {
        super(message, cause);
    }

}