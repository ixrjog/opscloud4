package com.baiyi.opscloud.common.exception.am;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2022/8/10 13:39
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateUserException extends BaseException {

    @Serial
    private static final long serialVersionUID = -2938977933351653100L;
    private final Integer code = 10009;

    public CreateUserException(String message) {
        super(message);
        setCode(code);
    }

    public CreateUserException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public CreateUserException(String message, Throwable cause) {
        super(message, cause);
    }

}