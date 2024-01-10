package com.baiyi.opscloud.workorder.helper;

import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.workorder.util.WorkflowUtil;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrder;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketNode;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkflowVO;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.service.workorder.WorkOrderService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketNodeService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketService;
import com.baiyi.opscloud.workorder.constants.OrderTicketPhaseCodeConstants;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 工单审批助手
 *
 * @Author baiyi
 * @Date 2022/1/19 9:40 AM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class TicketApproveHelper {

    private final WorkOrderService workOrderService;

    private final WorkOrderTicketService ticketService;

    private final WorkOrderTicketNodeService ticketNodeService;

    private final UserService userService;

    public void wrap(WorkOrderTicketVO.IApprover iApprover) {
        if (iApprover.getTicketId() == null) {
            return;
        }
        WorkOrderTicket workOrderTicket = ticketService.getById(iApprover.getTicketId());
        wrap(iApprover, workOrderTicket);
    }

    public void wrap(WorkOrderTicketVO.IApprover iApprover, WorkOrderTicket workOrderTicket) {
        if (workOrderTicket == null) {
            return;
        }
        // 设置默认值
        iApprover.setIsApprover(false);
        // 不在审批中
        if (!OrderTicketPhaseCodeConstants.TOAUDIT.name().equals(workOrderTicket.getTicketPhase())) {
            return;
        }
        final String username = SessionHolder.getUsername();
        WorkOrderTicketNode ticketNode = ticketNodeService.getById(workOrderTicket.getNodeId());
        if (!StringUtils.isEmpty(ticketNode.getUsername())) {
            // 判断是否为当前用户
            iApprover.setIsApprover(ticketNode.getUsername().equals(username));
        } else {
            WorkOrder workOrder = workOrderService.getById(workOrderTicket.getWorkOrderId());
            Map<String, WorkflowVO.Node> nodeMap = WorkflowUtil.toNodeMap(workOrder.getWorkflow());

            if (!nodeMap.containsKey(ticketNode.getNodeName())) {
                return;
            }
            WorkflowVO.Node node = nodeMap.get(ticketNode.getNodeName());
            List<User> users = userService.queryByTagKeys(node.getTags());
            Optional<User> userOptional = users
                    .stream()
                    .filter(u -> username.equals(u.getUsername()))
                    .findFirst();
            iApprover.setIsApprover(userOptional.isPresent());
        }
    }

}