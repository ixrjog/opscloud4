package com.baiyi.opscloud.leo.exception;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2022/12/29 19:34
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class LeoInterceptorException extends BaseException {

    @Serial
    private static final long serialVersionUID = -3978288350964809573L;

    private Integer code;

    public LeoInterceptorException(String message) {
        super(message);
        this.code = 42100;
    }

    public LeoInterceptorException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = 42100;
    }

}