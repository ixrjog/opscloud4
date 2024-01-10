package com.baiyi.opscloud.common.exception.asset;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2022/8/16 13:34
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ListEntitiesException extends BaseException {

    @Serial
    private static final long serialVersionUID = 520938458076138276L;
    private final Integer code = 10005;

    public ListEntitiesException(String message) {
        super(message);
        setCode(code);
    }

    public ListEntitiesException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public ListEntitiesException(String message, Throwable cause) {
        super(message, cause);
    }

}