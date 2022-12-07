package com.baiyi.opscloud.leo.exception;

import com.baiyi.opscloud.common.exception.BaseException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.slf4j.helpers.MessageFormatter;

/**
 * @Author baiyi
 * @Date 2022/12/5 19:40
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class LeoDeployException extends BaseException {

    private static final long serialVersionUID = -3109061002853891508L;
    private Integer code;

    public LeoDeployException(String message) {
        super(message);
        this.code = 43000;
    }

    public LeoDeployException(String message, Object... var2) {
        super(MessageFormatter.arrayFormat(message, var2).getMessage());
        this.code = 43000;
    }

}

