package com.baiyi.opscloud.common.exception;

import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2020/4/19 12:17 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public abstract class BaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6758520149628532498L;

    private Integer code;

    public BaseException(String message) {
        super(message);
        this.code = 10000;
    }

    public BaseException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = 10000;
    }

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }

}