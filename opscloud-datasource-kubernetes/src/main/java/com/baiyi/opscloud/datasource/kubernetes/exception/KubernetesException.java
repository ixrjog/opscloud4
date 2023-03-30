package com.baiyi.opscloud.datasource.kubernetes.exception;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.helpers.MessageFormatter;

/**
 * @Author baiyi
 * @Date 2022/10/24 19:48
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KubernetesException extends BaseException {

    private static final long serialVersionUID = 596212694251808845L;
    private final Integer code = 11001;

    public KubernetesException(String message) {
        super(message);
        setCode(code);
    }

    public KubernetesException(String message, Object... var2) {
        super(MessageFormatter.arrayFormat(message, var2).getMessage());
        setCode(code);
    }

    public KubernetesException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public KubernetesException(String message, Throwable cause) {
        super(message, cause);
    }
}
