package com.baiyi.opscloud.service.workorder;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/6 7:43 PM
 * @Version 1.0
 */
public interface WorkOrderTicketEntryService {

    WorkOrderTicketEntry getById(Integer id);

    void deleteById(Integer id);

    void add(WorkOrderTicketEntry workOrderTicketEntry);

    void update(WorkOrderTicketEntry workOrderTicketEntry);

    List<WorkOrderTicketEntry> queryByWorkOrderTicketId(Integer workOrderTicketId);

    int countByWorkOrderTicketId(Integer workOrderTicketId);

    int countByTicketUniqueKey(WorkOrderTicketEntry workOrderTicketEntry);

}