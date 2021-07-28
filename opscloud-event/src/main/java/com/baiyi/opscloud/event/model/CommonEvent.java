package com.baiyi.opscloud.event.model;

import org.springframework.context.ApplicationEvent;

/**
 * @Author baiyi
 * @Date 2021/7/27 2:54 下午
 * @Version 1.0
 */
public class CommonEvent extends ApplicationEvent {
    public CommonEvent(Object source, String message) {
        super(source);
    }
}
