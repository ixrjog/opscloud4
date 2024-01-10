package com.baiyi.opscloud.service.event.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Event;
import com.baiyi.opscloud.domain.param.event.EventParam;
import com.baiyi.opscloud.mapper.EventMapper;
import com.baiyi.opscloud.service.event.EventService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/10/9 2:27 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventMapper eventMapper;

    @Override
    public void add(Event event) {
        eventMapper.insert(event);
    }

    @Override
    public void update(Event event) {
        eventMapper.updateByPrimaryKey(event);
    }

    @Override
    public void updateByExampleSelective(Event event) {
        eventMapper.updateByPrimaryKeySelective(event);
    }

    @Override
    public List<Event> queryEventByInstance(String instanceUuid) {
        Example example = new Example(Event.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceUuid", instanceUuid)
                .andEqualTo("isActive", true);
        return eventMapper.selectByExample(example);
    }

    @Override
    public Event getByUniqueKey(String instanceUuid, String eventId) {
        Example example = new Example(Event.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceUuid", instanceUuid)
                .andEqualTo("eventId", eventId);
        return eventMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<Event> queryUserPermissionEventByParam(EventParam.UserPermissionEventPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<Event> data = eventMapper.queryUserPermissionEventPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public DataTable<Event> queryUserPermissionServerEventByParam(EventParam.UserPermissionEventPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<Event> data = eventMapper.queryUserPermissionServerEventPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

}