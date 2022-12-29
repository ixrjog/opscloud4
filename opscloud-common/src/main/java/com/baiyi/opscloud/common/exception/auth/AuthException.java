package com.baiyi.opscloud.common.exception.auth;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2021/5/13 4:05 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AuthException extends BaseException {

    private static final long serialVersionUID = -1331639499184065986L;
    private Integer code;

    public AuthException(String message) {
        super(message);
        this.code = 999;
    }

    public AuthException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public AuthException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }
}

