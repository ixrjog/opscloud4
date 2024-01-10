package com.baiyi.opscloud.leo.supervisor.base;

/**
 * @Author baiyi
 * @Date 2022/11/9 11:49
 * @Version 1.0
 */
public interface ISupervisor extends Runnable {

    boolean tryStop();

    boolean isTimeout();

}