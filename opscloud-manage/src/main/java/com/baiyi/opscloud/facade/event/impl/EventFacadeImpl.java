package com.baiyi.opscloud.facade.event.impl;

import com.baiyi.opscloud.domain.generator.opscloud.Event;
import com.baiyi.opscloud.facade.event.EventFacade;
import com.baiyi.opscloud.service.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @Author baiyi
 * @Date 2021/10/9 2:58 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class EventFacadeImpl implements EventFacade {

    private final EventService eventService;

    @Override
    @Async
    public void recordEvent(Event event){
        eventService.add(event);
    }

}
