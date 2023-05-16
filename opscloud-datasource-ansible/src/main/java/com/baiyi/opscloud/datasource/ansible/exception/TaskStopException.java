package com.baiyi.opscloud.datasource.ansible.exception;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2020/4/17 7:43 下午
 * @Version 1.0
 */
public class TaskStopException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2575786728049685346L;

    private final static String MESSAGE = "Task cancelled";

    public TaskStopException(){
        //父类构造方法
        super(MESSAGE);
    }

}
