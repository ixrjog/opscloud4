package com.baiyi.opscloud.workorder.helper.strategy.impl;

import com.baiyi.opscloud.common.util.WorkflowUtil;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.notice.INoticeMessage;
import com.baiyi.opscloud.domain.vo.workorder.WorkflowVO;
import com.baiyi.opscloud.workorder.constants.NodeTypeConstants;
import com.baiyi.opscloud.workorder.constants.OrderTicketPhaseCodeConstants;
import com.baiyi.opscloud.workorder.helper.strategy.base.AbstractSendNotice;
import com.baiyi.opscloud.workorder.model.TicketNoticeModel;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.baiyi.opscloud.datasource.manager.base.NoticeManager.MsgKeys.TICKET_APPROVE;

/**
 * @Author baiyi
 * @Date 2022/1/27 4:46 PM
 * @Version 1.0
 */
@Component
public class SendAuditNotice extends AbstractSendNotice {

    @Override
    public Set<String> getPhases() {
        return Sets.newHashSet(OrderTicketPhaseCodeConstants.TOAUDIT.getPhase());
    }

    @Override
    public void send(WorkOrderTicket ticket) {
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        Map<String, WorkflowVO.Node> nodeMap = WorkflowUtil.toWorkflowNodeMap(workOrder.getWorkflow());
        WorkOrderTicketNode node = ticketNodeService.getById(ticket.getNodeId());
        if (nodeMap.containsKey(node.getNodeName())) {
            INoticeMessage noticeMessage = buildNoticeMessage(ticket);
            WorkflowVO.Node workflowNode = nodeMap.get(node.getNodeName());
            if (NodeTypeConstants.SYS.getCode() == workflowNode.getType()) {
                // 广播
                List<User> auditUsers = userService.queryByTagKeys(workflowNode.getTags());
                send(auditUsers, TICKET_APPROVE, noticeMessage);
            }
            if (NodeTypeConstants.USER_LIST.getCode() == workflowNode.getType()) {
                // 单播
                User auditUser = userService.getByUsername(node.getUsername());
                send(Lists.newArrayList(auditUser), TICKET_APPROVE, noticeMessage);
            }
        }
    }

    @Override
    protected INoticeMessage buildNoticeMessage(WorkOrderTicket ticket) {
        // 工单创建者
        User user = userService.getByUsername(ticket.getUsername());
        String userDisplayName = user.getUsername() + "<" + Joiner.on(":").skipNulls().join(user.getDisplayName(), user.getName()) + ">";
        // 工单名称
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        // 工单条目
        List<WorkOrderTicketEntry> ticketEntries = ticketEntryService.queryByWorkOrderTicketId(ticket.getId());
        return TicketNoticeModel.ApproveNoticeMessage.builder()
                .ticketId(ticket.getId())
                .createUser(userDisplayName)
                .workOrderName(workOrder.getName())
                .ticketEntities(ticketEntries)
                .build();
    }

}
