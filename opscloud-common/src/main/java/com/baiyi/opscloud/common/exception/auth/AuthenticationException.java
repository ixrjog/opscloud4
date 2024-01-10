package com.baiyi.opscloud.common.exception.auth;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 身份认证
 * @Author baiyi
 * @Date 2021/5/13 4:05 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AuthenticationException extends BaseException {

    @Serial
    private static final long serialVersionUID = -1331639499184065986L;

    private Integer code;

    public AuthenticationException(String message) {
        super(message);
        this.code = 999;
    }

    public AuthenticationException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public AuthenticationException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }

}