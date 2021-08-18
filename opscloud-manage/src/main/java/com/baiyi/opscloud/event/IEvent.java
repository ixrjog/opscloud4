package com.baiyi.opscloud.event;

/**
 * @Author baiyi
 * @Date 2021/8/17 4:26 下午
 * @Version 1.0
 */
public interface IEvent<T> extends IEventType {

    String getAction();

    T getBody();

}
