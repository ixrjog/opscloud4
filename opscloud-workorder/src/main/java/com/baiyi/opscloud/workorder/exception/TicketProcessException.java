package com.baiyi.opscloud.workorder.exception;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2022/1/7 1:23 PM
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TicketProcessException extends BaseException {

    @Serial
    private static final long serialVersionUID = -9089909260424309939L;

    private final Integer code = 702;

    public TicketProcessException(String message) {
        super(message);
        setCode(code);
    }

    public TicketProcessException(String message, Object... var2) {
        super(message,var2);
        setCode(code);
    }

    public TicketProcessException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public TicketProcessException(String message, Throwable cause) {
        super(message, cause);
    }

}