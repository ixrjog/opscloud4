package com.baiyi.opscloud.facade.workorder.impl;

import com.baiyi.opscloud.workorder.util.WorkflowUtil;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrder;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketNode;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkflowVO;
import com.baiyi.opscloud.facade.workorder.WorkOrderTicketNodeFacade;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketNodeService;
import com.baiyi.opscloud.workorder.constants.NodeTypeConstants;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

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
        WorkflowVO.Workflow workflowVO = WorkflowUtil.load(workOrder.getWorkflow());
        List<WorkOrderTicketNode> nodes = Lists.newArrayList();
        for (WorkflowVO.Node node : workflowVO.getNodes()) {
            WorkOrderTicketNode workOrderTicketNode = WorkOrderTicketNode.builder()
                    .workOrderTicketId(workOrderTicket.getId())
                    .nodeName(node.getName())
                    .parentId(CollectionUtils.isEmpty(nodes) ? 0 : nodes.get(nodes.size() - 1).getId())
                    .comment(node.getComment())
                    .build();
            workOrderTicketNodeService.add(workOrderTicketNode);
            nodes.add(workOrderTicketNode);
        }
    }

    @Override
    public void verifyWorkflowNodes(WorkOrder workOrder, WorkOrderTicket workOrderTicket) {
        Map<String, WorkflowVO.Node> nodeMap = WorkflowUtil.toNodeMap(workOrder.getWorkflow());
        List<WorkOrderTicketNode> nodes = workOrderTicketNodeService.queryByWorkOrderTicketId(workOrderTicket.getId());
        for (WorkOrderTicketNode node : nodes) {
            if (!nodeMap.containsKey(node.getNodeName())) {
                throw new TicketVerifyException("工单验证失败: 未找到 {} 审批节点！", node.getNodeName());
            }
            if (NodeTypeConstants.USER_LIST.getCode() == nodeMap.get(node.getNodeName()).getType()) {
                // 用户必须指定
                if (StringUtils.isEmpty(node.getUsername())) {
                    throw new TicketVerifyException("工单验证失败: {} 审批节点必须指定审批人！", node.getNodeName());
                }
            }
        }
    }

    @Override
    public void updateWorkflowNodeAuditUser(int workOrderTicketId, String nodeName, UserVO.User auditUser) {
        WorkOrderTicketNode workOrderTicketNode = workOrderTicketNodeService.getByUniqueKey(workOrderTicketId, nodeName);
        if (workOrderTicketNode == null) {
            return;
        }
        workOrderTicketNode.setUsername(auditUser.getUsername());
        workOrderTicketNodeService.update(workOrderTicketNode);
    }

}
