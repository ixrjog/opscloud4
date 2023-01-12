package com.baiyi.opscloud.leo.exception;

import com.baiyi.opscloud.common.exception.BaseException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.slf4j.helpers.MessageFormatter;

/**
 * @Author baiyi
 * @Date 2022/12/29 19:34
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class LeoInterceptorException extends BaseException {

    private static final long serialVersionUID = -3978288350964809573L;

    private Integer code;

    public LeoInterceptorException(String message) {
        super(message);
        this.code = 42100;
    }

    public LeoInterceptorException(String message, Object... var2) {
        super(MessageFormatter.arrayFormat(message, var2).getMessage());
        this.code = 42100;
    }

}
