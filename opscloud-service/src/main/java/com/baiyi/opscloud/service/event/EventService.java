package com.baiyi.opscloud.service.event;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Event;
import com.baiyi.opscloud.domain.param.event.EventParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/10/9 2:26 下午
 * @Version 1.0
 */
public interface EventService {

    void add(Event event);

    void update(Event event);

    void updateByExampleSelective(Event event);

    List<Event> queryEventByInstance(String instanceUuid);

    Event getByUniqueKey(String instanceUuid, String eventId);

    DataTable<Event> queryUserPermissionEventByParam(EventParam.UserPermissionEventPageQuery pageQuery);

    DataTable<Event> queryUserPermissionServerEventByParam(EventParam.UserPermissionEventPageQuery pageQuery);

}