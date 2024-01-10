package com.baiyi.opscloud.datasource.kubernetes.exception;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2022/10/24 19:48
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KubernetesException extends BaseException {

    @Serial
    private static final long serialVersionUID = 596212694251808845L;
    private final Integer code = 11001;

    public KubernetesException(String message) {
        super(message);
        setCode(code);
    }

    public KubernetesException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        setCode(code);
    }

    public KubernetesException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public KubernetesException(String message, Throwable cause) {
        super(message, cause);
    }

}