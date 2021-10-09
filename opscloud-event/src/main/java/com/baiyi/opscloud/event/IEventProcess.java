package com.baiyi.opscloud.event;

import com.baiyi.opscloud.event.enums.EventTypeEnum;

/**
 * @Author baiyi
 * @Date 2021/10/9 3:10 下午
 * @Version 1.0
 */
public interface IEventProcess {

    /**
     * 监听
     */
    void listener();

    EventTypeEnum getEventType();

}
