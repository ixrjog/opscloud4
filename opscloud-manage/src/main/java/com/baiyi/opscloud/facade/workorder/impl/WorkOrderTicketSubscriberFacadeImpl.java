package com.baiyi.opscloud.facade.workorder.impl;

import com.baiyi.opscloud.common.util.PasswordUtil;
import com.baiyi.opscloud.workorder.util.WorkflowUtil;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.vo.workorder.WorkflowVO;
import com.baiyi.opscloud.facade.workorder.WorkOrderTicketSubscriberFacade;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.service.workorder.WorkOrderService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketNodeService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketSubscriberService;
import com.baiyi.opscloud.workorder.constants.NodeTypeConstants;
import com.baiyi.opscloud.workorder.constants.SubscribeStatusConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/1/18 10:23 AM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class WorkOrderTicketSubscriberFacadeImpl implements WorkOrderTicketSubscriberFacade {

    private final WorkOrderService workOrderService;

    private final WorkOrderTicketSubscriberService ticketSubscriberService;

    private final WorkOrderTicketNodeService ticketNodeService;

    private final UserService userService;

    /**
     * createSubscriber
     * 创建订阅人（创建人）
     *
     * @param ticket
     * @param user
     */
    @Override
    public void publish(WorkOrderTicket ticket, User user) {
        createSubscriber(ticket, user, SubscribeStatusConstants.CREATE);
    }

    /**
     * 发布到所有参与审批的用户
     *
     * @param ticket
     */
    @Override
    public void publish(WorkOrderTicket ticket) {
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        Map<String, WorkflowVO.Node> nodeMap = WorkflowUtil.toNodeMap(workOrder.getWorkflow());
        List<WorkOrderTicketNode> ticketNodes = ticketNodeService.queryByWorkOrderTicketId(ticket.getId());
        ticketNodes.forEach(n -> {
            if (nodeMap.containsKey(n.getNodeName())) {
                WorkflowVO.Node workflowNode = nodeMap.get(n.getNodeName());
                if (NodeTypeConstants.SYS.getCode() == workflowNode.getType()) {
                    // 广播
                    List<User> auditUsers = userService.queryByTagKeys(workflowNode.getTags());
                    auditUsers.forEach(auditUser -> createSubscriber(ticket, auditUser, SubscribeStatusConstants.AUDIT));
                    return;
                }
                if (NodeTypeConstants.USER_LIST.getCode() == workflowNode.getType()) {
                    // 单播
                    User auditUser = userService.getByUsername(n.getUsername());
                    createSubscriber(ticket, auditUser, SubscribeStatusConstants.AUDIT);
                }
            }
        });
    }

    private void createSubscriber(WorkOrderTicket ticket, User user, SubscribeStatusConstants constants) {
        WorkOrderTicketSubscriber subscriber = WorkOrderTicketSubscriber.builder()
                .workOrderTicketId(ticket.getId())
                .userId(user.getId())
                .username(user.getUsername())
                .subscribeStatus(constants.name())
                .isActive(true)
                // 生成64位长度随机Token
                .token(PasswordUtil.generatorPW(64))
                .comment(constants.getComment())
                .build();
        ticketSubscriberService.add(subscriber);
    }

}
