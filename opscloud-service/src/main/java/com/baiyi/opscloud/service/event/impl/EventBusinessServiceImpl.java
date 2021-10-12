package com.baiyi.opscloud.service.event.impl;

import com.baiyi.opscloud.domain.generator.opscloud.EventBusiness;
import com.baiyi.opscloud.mapper.opscloud.EventBusinessMapper;
import com.baiyi.opscloud.service.event.EventBusinessService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/10/11 5:54 下午
 * @Version 1.0
 */
@Service
public class EventBusinessServiceImpl implements EventBusinessService {

    @Resource
    private EventBusinessMapper eventBusinessMapper;

    @Override
    public void add(EventBusiness eventBusiness) {
        eventBusinessMapper.insert(eventBusiness);
    }

    @Override
    public List<EventBusiness> queryByEventId(Integer eventId) {
        Example example = new Example(EventBusiness.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("eventId", eventId);
        return eventBusinessMapper.selectByExample(example);
    }

    @Override
    public EventBusiness getById(Integer id) {
        return eventBusinessMapper.selectByPrimaryKey(id);
    }

}
