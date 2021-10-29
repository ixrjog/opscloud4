package com.baiyi.opscloud.facade.event.impl;

import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.domain.generator.opscloud.Event;
import com.baiyi.opscloud.facade.event.EventFacade;
import com.baiyi.opscloud.service.event.EventService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/10/9 2:58 下午
 * @Version 1.0
 */
@Service
public class EventFacadeImpl implements EventFacade {

    @Resource
    private EventService eventService;

    @Override
    @Async(value = Global.TaskPools.DEFAULT)
    public void recordEvent(Event event){
        eventService.add(event);
    }

}
