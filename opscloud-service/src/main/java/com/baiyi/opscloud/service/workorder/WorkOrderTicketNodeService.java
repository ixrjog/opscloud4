package com.baiyi.opscloud.service.workorder;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketNode;

/**
 * @Author baiyi
 * @Date 2022/1/14 4:11 PM
 * @Version 1.0
 */
public interface WorkOrderTicketNodeService {

    void add(WorkOrderTicketNode workOrderTicketNode);

    void update(WorkOrderTicketNode workOrderTicketNode);
}
