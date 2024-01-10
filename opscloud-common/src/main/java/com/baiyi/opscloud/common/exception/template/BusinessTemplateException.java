package com.baiyi.opscloud.common.exception.template;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2023/9/26 13:54
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessTemplateException extends BaseException {

    @Serial
    private static final long serialVersionUID = 2805163678079518161L;

    private final Integer code = 10022;

    public BusinessTemplateException(String message) {
        super(message);
        setCode(code);
    }

    public BusinessTemplateException(String message, Object... var2) {
        super(message,var2);
        setCode(code);
    }

    public BusinessTemplateException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public BusinessTemplateException(String message, Throwable cause) {
        super(message, cause);
    }

}