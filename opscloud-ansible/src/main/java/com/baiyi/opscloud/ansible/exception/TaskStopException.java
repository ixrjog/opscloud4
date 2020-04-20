package com.baiyi.opscloud.ansible.exception;

/**
 * @Author baiyi
 * @Date 2020/4/17 7:43 下午
 * @Version 1.0
 */
public class TaskStopException extends RuntimeException {

    private final static String message = "Task cancelled";

    public TaskStopException(){	//构造方法
        super(message);		//父类构造方法
    }


}
