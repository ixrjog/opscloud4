package com.baiyi.opscloud.ansible.exception;

/**
 * @Author baiyi
 * @Date 2020/4/17 7:46 下午
 * @Version 1.0
 */
public class TaskMemberStopException extends RuntimeException{

    private final static String message = "Task member timeout";

    public TaskMemberStopException(){	//构造方法
        super(message);		//父类构造方法
    }
}
