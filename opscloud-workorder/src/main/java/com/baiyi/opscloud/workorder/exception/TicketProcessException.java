package com.baiyi.opscloud.workorder.exception;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2022/1/7 1:23 PM
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TicketProcessException extends BaseException {

    private static final long serialVersionUID = -9089909260424309939L;

    private final Integer code = 700;

    public TicketProcessException(String message) {
        super(message);
        setCode(code);
    }

    public TicketProcessException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public TicketProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}

