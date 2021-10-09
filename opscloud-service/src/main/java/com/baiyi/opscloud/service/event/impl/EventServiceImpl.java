package com.baiyi.opscloud.service.event.impl;

import com.baiyi.opscloud.domain.generator.opscloud.Event;
import com.baiyi.opscloud.mapper.opscloud.EventMapper;
import com.baiyi.opscloud.service.event.EventService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/10/9 2:27 下午
 * @Version 1.0
 */
@Service
public class EventServiceImpl implements EventService {

    @Resource
    private EventMapper eventMapper;

    @Override
    public void add(Event event){
        eventMapper.insert(event);
    }

    @Override
    public void update(Event event){
        eventMapper.updateByPrimaryKey(event);
    }

}
