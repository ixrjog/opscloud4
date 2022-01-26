package com.baiyi.opscloud.service.workorder;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketParam;

/**
 * @Author baiyi
 * @Date 2022/1/6 7:54 PM
 * @Version 1.0
 */
public interface WorkOrderTicketService {

    DataTable<WorkOrderTicket> queryPageByParam(WorkOrderTicketParam.TicketPageQuery pageQuery);

    void add(WorkOrderTicket workOrderTicket);

    void update(WorkOrderTicket workOrderTicket);

    WorkOrderTicket getById(int id);

    WorkOrderTicket getNewTicketByUser(String workOrderKey, String username);

    void deleteById(int id);

}
