package com.baiyi.opscloud.service.workorder;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketSubscriber;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/18 10:17 AM
 * @Version 1.0
 */
public interface WorkOrderTicketSubscriberService {

    void add(WorkOrderTicketSubscriber workOrderTicketSubscriber);

    WorkOrderTicketSubscriber getByUniqueKey(WorkOrderTicketSubscriber workOrderTicketSubscriber);

    void update(WorkOrderTicketSubscriber workOrderTicketSubscriber);

    List<WorkOrderTicketSubscriber> queryByWorkOrderTicketId(int workOrderTicketId);

    void deleteById(int id);

}