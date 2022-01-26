package com.baiyi.opscloud.workorder.helper;

import com.baiyi.opscloud.common.util.WorkflowUtil;
import com.baiyi.opscloud.datasource.manager.base.NoticeManager;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.notice.INoticeMessage;
import com.baiyi.opscloud.domain.vo.workorder.WorkflowVO;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.service.workorder.WorkOrderService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketEntryService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketNodeService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketService;
import com.baiyi.opscloud.workorder.constants.NodeTypeConstants;
import com.baiyi.opscloud.workorder.constants.OrderPhaseCodeConstants;
import com.baiyi.opscloud.workorder.model.TicketNoticeModel;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

import static com.baiyi.opscloud.common.config.ThreadPoolTaskConfiguration.TaskPools.CORE;
import static com.baiyi.opscloud.datasource.manager.base.NoticeManager.MsgKeys.TICKET_APPROVE;
import static com.baiyi.opscloud.datasource.manager.base.NoticeManager.MsgKeys.TICKET_END;

/**
 * 工单通知助手
 *
 * @Author baiyi
 * @Date 2022/1/26 3:08 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class TicketNoticeHelper {

    private final WorkOrderTicketEntryService ticketEntryService;

    private final WorkOrderTicketService ticketService;

    private final WorkOrderTicketNodeService ticketNodeService;

    private final WorkOrderService workOrderService;

    private final UserService userService;

    private final NoticeManager noticeManager;

    /**
     * 发送通知
     *
     * @param ticket
     */
    @Async(CORE)
    public void notice(WorkOrderTicket ticket) {
        final String phase = ticket.getTicketPhase();
        // 审批通知
        if (OrderPhaseCodeConstants.TOAUDIT.name().equals(phase)) {
            WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
            Map<String, WorkflowVO.Node> nodeMap = WorkflowUtil.toWorkflowNodeMap(workOrder.getWorkflow());
            WorkOrderTicketNode node = ticketNodeService.getById(ticket.getNodeId());
            if (nodeMap.containsKey(node.getNodeName())) {
                INoticeMessage noticeMessage = buildApproveNoticeMessage(ticket);
                WorkflowVO.Node workflowNode = nodeMap.get(node.getNodeName());
                if (NodeTypeConstants.SYS.getCode() == workflowNode.getType()) {
                    // 广播
                    List<User> auditUsers = userService.queryByTagKeys(workflowNode.getTags());
                    notice(auditUsers, TICKET_APPROVE, noticeMessage);
                }
                if (NodeTypeConstants.USER_LIST.getCode() == workflowNode.getType()) {
                    // 单播
                    User auditUser = userService.getByUsername(node.getUsername());
                    notice(Lists.newArrayList(auditUser), TICKET_APPROVE, noticeMessage);
                }
            }
            return;
        }
        // 结束通知
        User user = userService.getByUsername(ticket.getUsername());
        if (OrderPhaseCodeConstants.SUCCESS.name().equals(phase)) {
            notice(Lists.newArrayList(user), TICKET_END, buildEndNoticeMessage(ticket, "工单执行成功！"));
        }
        if (OrderPhaseCodeConstants.FAILED.name().equals(phase)) {
            notice(Lists.newArrayList(user), TICKET_END, buildEndNoticeMessage(ticket, "工单执行失败！"));
        }
        if (OrderPhaseCodeConstants.REJECT.name().equals(phase)) {
            notice(Lists.newArrayList(user), TICKET_END, buildEndNoticeMessage(ticket, "工单审批被拒绝！"));
        }
    }

    private void notice(List<User> users, String msgKey, INoticeMessage noticeMessage) {
        if (CollectionUtils.isEmpty(users))
            return;
        users.forEach(user ->
                noticeManager.sendMessage(user, msgKey, noticeMessage)
        );
    }

    private TicketNoticeModel.ApproveNoticeMessage buildApproveNoticeMessage(WorkOrderTicket ticket) {
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

    private TicketNoticeModel.EndNoticeMessage buildEndNoticeMessage(WorkOrderTicket ticket, String result) {
        // 工单名称
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        // 工单条目
        List<WorkOrderTicketEntry> ticketEntries = ticketEntryService.queryByWorkOrderTicketId(ticket.getId());
        return TicketNoticeModel.EndNoticeMessage.builder()
                .ticketId(ticket.getId())
                .workOrderName(workOrder.getName())
                .result(result)
                .build();
    }


}
