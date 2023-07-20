package com.baiyi.opscloud.leo.exception;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2022/11/7 13:58
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class LeoJobException extends BaseException {

    @Serial
    private static final long serialVersionUID = 4679996919812759224L;

    private Integer code;

    public LeoJobException(String message) {
        super(message);
        this.code = 41000;
    }

    public LeoJobException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = 41000;
    }

}