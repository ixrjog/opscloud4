package com.baiyi.opscloud.service.workorder.impl;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketSubscriber;
import com.baiyi.opscloud.mapper.opscloud.WorkOrderTicketSubscriberMapper;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketSubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        workOrderTicketSubscriberMapper.insert(workOrderTicketSubscriber);
    }

    @Override
    public void update(WorkOrderTicketSubscriber workOrderTicketSubscriber) {
        workOrderTicketSubscriberMapper.updateByPrimaryKey(workOrderTicketSubscriber);
    }

}
