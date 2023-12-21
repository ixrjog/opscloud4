package com.baiyi.opscloud.workorder.exception;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2022/1/10 3:43 PM
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TicketVerifyException extends BaseException {

    @Serial
    private static final long serialVersionUID = -7136556927011864675L;

    private final Integer code = 701;

    public TicketVerifyException(String message) {
        super(message);
        setCode(code);
    }

    public TicketVerifyException(String message, Object... var2) {
        super(message,var2);
        setCode(code);
    }

    public TicketVerifyException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public TicketVerifyException(String message, Throwable cause) {
        super(message, cause);
    }

}