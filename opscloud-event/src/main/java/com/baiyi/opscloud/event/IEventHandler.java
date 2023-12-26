package com.baiyi.opscloud.event;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Event;
import com.baiyi.opscloud.domain.param.event.EventParam;
import com.baiyi.opscloud.event.enums.EventTypeEnum;

/**
 * @Author baiyi
 * @Date 2021/10/9 3:10 下午
 * @Version 1.0
 */
public interface IEventHandler {

    DataTable<Event> listEvent(EventParam.UserPermissionEventPageQuery pageQuery);

    /**
     * 监听
     */
    void listener();

    EventTypeEnum getEventType();

}