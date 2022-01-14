package com.baiyi.opscloud.facade.workorder.impl;

import com.baiyi.opscloud.common.util.WorkflowUtil;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrder;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketNode;
import com.baiyi.opscloud.domain.vo.workorder.WorkflowVO;
import com.baiyi.opscloud.facade.workorder.WorkOrderTicketNodeFacade;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketNodeService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/14 4:14 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class WorkOrderTicketNodeFacadeImpl implements WorkOrderTicketNodeFacade {

    private final WorkOrderTicketNodeService workOrderTicketNodeService;

    @Override
    public void createWorkflowNodes(WorkOrder workOrder, WorkOrderTicket workOrderTicket) {
        WorkflowVO.Workflow workflowVO = WorkflowUtil.toWorkflowView(workOrder.getWorkflow());
        List<WorkOrderTicketNode> nodes = Lists.newArrayList();
        workflowVO.getNodes().forEach(node -> {
            WorkOrderTicketNode workOrderTicketNode = WorkOrderTicketNode.builder()
                    .workOrderTicketId(workOrderTicket.getId())
                    .nodeName(node.getName())
                    .parentId(CollectionUtils.isEmpty(nodes) ? 0 : nodes.get(nodes.size() - 1).getId())
                    .comment(node.getComment())
                    .build();
            workOrderTicketNodeService.add(workOrderTicketNode);
            nodes.add(workOrderTicketNode);
        });

    }

}
