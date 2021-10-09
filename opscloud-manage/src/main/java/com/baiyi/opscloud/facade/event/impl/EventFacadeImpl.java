package com.baiyi.opscloud.facade.event.impl;

import com.baiyi.opscloud.facade.event.EventFacade;
import com.baiyi.opscloud.service.event.EventService;
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

}
