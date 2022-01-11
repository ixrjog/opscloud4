package com.baiyi.opscloud.workorder.exception;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2022/1/10 3:43 PM
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VerifyTicketEntryException extends BaseException {

    private static final long serialVersionUID = -7136556927011864675L;
    private final Integer code = 701;

    public VerifyTicketEntryException(String message) {
        super(message);
        setCode(code);
    }

    public VerifyTicketEntryException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public VerifyTicketEntryException(String message, Throwable cause) {
        super(message, cause);
    }
}

