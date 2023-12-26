package com.baiyi.opscloud.datasource.kubernetes.exception;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2022/7/18 14:51
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KubernetesDeploymentException extends BaseException {

    @Serial
    private static final long serialVersionUID = -7308795499204092235L;

    private final Integer code = 10001;

    public KubernetesDeploymentException(String message) {
        super(message);
        setCode(code);
    }

    public KubernetesDeploymentException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        setCode(code);
    }

    public KubernetesDeploymentException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public KubernetesDeploymentException(String message, Throwable cause) {
        super(message, cause);
    }

}