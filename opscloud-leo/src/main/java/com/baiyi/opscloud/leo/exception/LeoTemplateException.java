package com.baiyi.opscloud.leo.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.slf4j.helpers.MessageFormatter;

/**
 * @Author baiyi
 * @Date 2022/11/1 17:11
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class LeoTemplateException extends RuntimeException {

    private static final long serialVersionUID = 4679996919812759224L;

    private Integer code;

    public LeoTemplateException(String message) {
        super(message);
        this.code = 40000;
    }

    public LeoTemplateException(String message, Object... var2) {
        super(MessageFormatter.arrayFormat(message, var2).getMessage());
        this.code = 40000;
    }

}

