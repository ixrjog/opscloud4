package com.baiyi.opscloud.common.exception.ssh;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2021/6/11 4:26 下午
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SshCommonException extends BaseException {

    private final Integer code = 999;

    public SshCommonException(String message) {
        super(message);
        setCode(code);
    }

    public SshCommonException(ErrorEnum errorEnum) {
        super(errorEnum);
    }
}
