package com.baiyi.opscloud.workorder.exception;

import com.baiyi.opscloud.common.util.StringFormatter;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2022/1/13 10:37 AM
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
public class TicketException extends BaseTicketException {

    @Serial
    private static final long serialVersionUID = -5608146219860556302L;

    public TicketException(String message) {
        super(message);
    }

    public TicketException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
    }

}