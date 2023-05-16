package com.baiyi.opscloud.core.exception;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2022/10/27 14:33
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DatasourceProviderException extends BaseException {

    @Serial
    private static final long serialVersionUID = -2938977933351653100L;

    private final Integer code = 20000;

    public DatasourceProviderException(String message) {
        super(message);
        setCode(code);
    }

    public DatasourceProviderException(String message, Object... var2) {
        super(message, var2);
        setCode(code);
    }

    public DatasourceProviderException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public DatasourceProviderException(String message, Throwable cause) {
        super(message, cause);
    }

}