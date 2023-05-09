package com.baiyi.opscloud.datasource.ansible.exception;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2020/4/17 7:47 下午
 * @Version 1.0
 */
public class TaskTimeoutException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5132629163560589207L;

    private final static String MESSAGE = "Task timeout";

    public TaskTimeoutException() {
        super(MESSAGE);
    }

}
