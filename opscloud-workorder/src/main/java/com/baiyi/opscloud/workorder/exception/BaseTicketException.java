package com.baiyi.opscloud.workorder.exception;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2022/1/13 10:38 AM
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseTicketException extends BaseException {

    @Serial
    private static final long serialVersionUID = 6926889238773828276L;

    private final Integer code = 700;

    public BaseTicketException(String message) {
        super(message);
        setCode(code);
    }

    public BaseTicketException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public BaseTicketException(String message, Throwable cause) {
        super(message, cause);
    }

}