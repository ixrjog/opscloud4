package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.Event;
import com.baiyi.opscloud.domain.param.event.EventParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface EventMapper extends Mapper<Event> {

    List<Event> queryUserPermissionEventPageByParam(EventParam.UserPermissionEventPageQuery pageQuery);

    List<Event> queryUserPermissionServerEventPageByParam(EventParam.UserPermissionEventPageQuery pageQuery);

}