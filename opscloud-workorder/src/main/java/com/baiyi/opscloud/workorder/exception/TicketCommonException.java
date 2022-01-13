package com.baiyi.opscloud.workorder.exception;

import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2022/1/13 10:37 AM
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
public class TicketCommonException extends BaseTicketException {
    private static final long serialVersionUID = -5608146219860556302L;

    public TicketCommonException(String message) {
        super(message);
    }
}
