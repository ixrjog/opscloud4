package com.baiyi.opscloud.service.workorder.impl;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketSubscriber;
import com.baiyi.opscloud.mapper.WorkOrderTicketSubscriberMapper;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketSubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/18 10:18 AM
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class WorkOrderTicketSubscriberServiceImpl implements WorkOrderTicketSubscriberService {

    private final WorkOrderTicketSubscriberMapper workOrderTicketSubscriberMapper;

    @Override
    public void add(WorkOrderTicketSubscriber workOrderTicketSubscriber) {
        if (getByUniqueKey(workOrderTicketSubscriber) == null)
            workOrderTicketSubscriberMapper.insert(workOrderTicketSubscriber);
    }

    @Override
    public void update(WorkOrderTicketSubscriber workOrderTicketSubscriber) {
        workOrderTicketSubscriberMapper.updateByPrimaryKey(workOrderTicketSubscriber);
    }

    @Override
    public WorkOrderTicketSubscriber getByUniqueKey(WorkOrderTicketSubscriber workOrderTicketSubscriber) {
        Example example = new Example(WorkOrderTicketSubscriber.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderTicketId", workOrderTicketSubscriber.getWorkOrderTicketId())
                .andEqualTo("username", workOrderTicketSubscriber.getUsername())
                .andEqualTo("subscribeStatus", workOrderTicketSubscriber.getSubscribeStatus());
        return workOrderTicketSubscriberMapper.selectOneByExample(example);
    }

    @Override
    public List<WorkOrderTicketSubscriber> queryByWorkOrderTicketId(int workOrderTicketId) {
        Example example = new Example(WorkOrderTicketSubscriber.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderTicketId", workOrderTicketId);
        return workOrderTicketSubscriberMapper.selectByExample(example);
    }

    @Override
    public void deleteById(int id) {
        workOrderTicketSubscriberMapper.deleteByPrimaryKey(id);
    }

}