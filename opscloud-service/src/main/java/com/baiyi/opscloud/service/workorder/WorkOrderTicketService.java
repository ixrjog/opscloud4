package com.baiyi.opscloud.service.workorder;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;

/**
 * @Author baiyi
 * @Date 2022/1/6 7:54 PM
 * @Version 1.0
 */
public interface WorkOrderTicketService {

    void add(WorkOrderTicket workOrderTicket);

    WorkOrderTicket getById(int id);

    WorkOrderTicket getNewTicketByUser(String workOrderKey, String username);

}
