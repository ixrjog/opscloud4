package com.baiyi.opscloud.leo.exception;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2022/11/1 17:11
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class LeoTemplateException extends BaseException {

    @Serial
    private static final long serialVersionUID = 4679996919812759224L;

    private Integer code;

    public LeoTemplateException(String message) {
        super(message);
        this.code = 40000;
    }

    public LeoTemplateException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = 40000;
    }

}