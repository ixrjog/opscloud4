package com.baiyi.opscloud.ansible.exception;

/**
 * @Author baiyi
 * @Date 2020/4/27 9:52 上午
 * @Version 1.0
 */
public class TaskLogExceededLimit extends RuntimeException {

    private final static String message = "Task log exceeded limit";

    public TaskLogExceededLimit(){	//构造方法
        super(message);		//父类构造方法
    }

}

