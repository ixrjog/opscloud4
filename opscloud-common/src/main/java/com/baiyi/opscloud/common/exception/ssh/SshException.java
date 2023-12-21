package com.baiyi.opscloud.common.exception.ssh;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2021/6/11 4:26 下午
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SshException extends BaseException {

    @Serial
    private static final long serialVersionUID = -8405599962375367104L;

    private final Integer code = 999;

    public SshException(String message) {
        super(message);
        setCode(code);
    }

    public SshException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

}