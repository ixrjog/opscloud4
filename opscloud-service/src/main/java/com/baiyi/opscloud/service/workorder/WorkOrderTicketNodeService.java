package com.baiyi.opscloud.service.workorder;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketNode;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/14 4:11 PM
 * @Version 1.0
 */
public interface WorkOrderTicketNodeService {

    WorkOrderTicketNode getById(int id);

    void add(WorkOrderTicketNode workOrderTicketNode);

    void update(WorkOrderTicketNode workOrderTicketNode);

    List<WorkOrderTicketNode> queryByWorkOrderTicketId(int workOrderTicketId);

    WorkOrderTicketNode getByUniqueKey(int workOrderTicketId, String nodeName);

    WorkOrderTicketNode getByUniqueKey(int workOrderTicketId, int parentId);

    void deleteById(int id);

}