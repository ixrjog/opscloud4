package com.baiyi.opscloud.leo.exception;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2022/11/8 16:39
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class LeoBuildException extends BaseException {

    @Serial
    private static final long serialVersionUID = -4973491757480514266L;

    private Integer code;

    public LeoBuildException(String message) {
        super(message);
        this.code = 42000;
    }

    public LeoBuildException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = 42000;
    }

}