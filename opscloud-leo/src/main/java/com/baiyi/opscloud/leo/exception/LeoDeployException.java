package com.baiyi.opscloud.leo.exception;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2022/12/5 19:40
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class LeoDeployException extends BaseException {

    @Serial
    private static final long serialVersionUID = -3109061002853891508L;
    private Integer code;

    public LeoDeployException(String message) {
        super(message);
        this.code = 43000;
    }

    public LeoDeployException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = 43000;
    }

}