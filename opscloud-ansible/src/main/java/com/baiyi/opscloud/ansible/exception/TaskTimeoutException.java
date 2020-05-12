package com.baiyi.opscloud.ansible.exception;

/**
 * @Author baiyi
 * @Date 2020/4/17 7:47 下午
 * @Version 1.0
 */
public class TaskTimeoutException extends RuntimeException{

    private final static String message = "Task timeout";

    public TaskTimeoutException(){	//构造方法
        super(message);		//父类构造方法
    }
}
