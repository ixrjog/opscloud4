package com.baiyi.opscloud.workorder.helper.strategy;

import com.baiyi.opscloud.common.util.WorkflowUtil;
import com.baiyi.opscloud.datasource.manager.base.NoticeManager;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.notice.INoticeMessage;
import com.baiyi.opscloud.domain.vo.workorder.WorkflowVO;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.service.workorder.WorkOrderService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketEntryService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketNodeService;
import com.baiyi.opscloud.workorder.constants.NodeTypeConstants;
import com.baiyi.opscloud.workorder.constants.OrderPhaseCodeConstants;
import com.baiyi.opscloud.workorder.model.TicketNoticeModel;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.baiyi.opscloud.datasource.manager.base.NoticeManager.MsgKeys.TICKET_APPROVE;
import static com.baiyi.opscloud.datasource.manager.base.NoticeManager.MsgKeys.TICKET_END;

/**
 * @Author 修远
 * @Date 2022/1/27 3:25 PM
 * @Since 1.0
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketNoticeStrategy {

    private final WorkOrderTicketEntryService ticketEntryService;

    private final WorkOrderTicketNodeService ticketNodeService;

    private final WorkOrderService workOrderService;

    private final UserService userService;

    private final NoticeManager noticeManager;

    private Map<OrderPhaseCodeConstants, BiConsumer<WorkOrderTicket, String>> noticeMap;

    @PostConstruct
    public void noticeMapInit() {
        noticeMap = ImmutableMap.<OrderPhaseCodeConstants, BiConsumer<WorkOrderTicket, String>>builder()
                .put(OrderPhaseCodeConstants.TOAUDIT, this::toAuditNotice)
                .put(OrderPhaseCodeConstants.SUCCESS, this::endNotice)
                .put(OrderPhaseCodeConstants.FAILED, this::endNotice)
                .put(OrderPhaseCodeConstants.REJECT, this::endNotice)
                .build();
    }

    public BiConsumer<WorkOrderTicket, String> getStrategyNotice(OrderPhaseCodeConstants codeConstants) {
        return noticeMap.get(codeConstants);
    }

    private void toAuditNotice(WorkOrderTicket ticket, String result) {
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
    }

    private void endNotice(WorkOrderTicket ticket, String result) {
        User user = userService.getByUsername(ticket.getUsername());
        notice(Lists.newArrayList(user), TICKET_END, buildEndNoticeMessage(ticket, result));
    }

    private void notice(List<User> users, String msgKey, INoticeMessage noticeMessage) {
        if (CollectionUtils.isEmpty(users)) return;
        users.forEach(user -> noticeManager.sendMessage(user, msgKey, noticeMessage));
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
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        return TicketNoticeModel.EndNoticeMessage.builder()
                .ticketId(ticket.getId())
                .workOrderName(workOrder.getName())
                .result(result)
                .build();
    }
}
