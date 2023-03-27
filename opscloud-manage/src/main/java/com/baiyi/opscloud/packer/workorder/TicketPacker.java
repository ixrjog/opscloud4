package com.baiyi.opscloud.packer.workorder;

import com.baiyi.opscloud.common.annotation.AgoWrapper;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.packer.user.UserAvatarPacker;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.workorder.helper.TicketApproveHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/1/11 3:14 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class TicketPacker implements IWrapper<WorkOrderTicketVO.Ticket> {

    private final UserService userService;

    private final TicketEntryPacker ticketEntryPacker;

    private final WorkOrderPacker workOrderPacker;

    private final WorkOrderWorkflowPacker workOrderWorkflowPacker;

    private final TicketNodePacker nodePacker;

    private final UserAvatarPacker userAvatarPacker;

    private final TicketApproveHelper ticketApproveHelper;

    @Override
    @AgoWrapper
    public void wrap(WorkOrderTicketVO.Ticket ticket, IExtend iExtend) {
        if (!iExtend.getExtend()) {
            return;
        }
        workOrderPacker.wrap(ticket);
        User user = userService.getByUsername(ticket.getUsername());
        ticket.setCreateUser(toCreateUser(ticket.getUsername()));
        ticketApproveHelper.wrap(ticket);
    }

    /**
     * 只包装工单配置条目
     *
     * @param ticketId
     * @return
     */
    public WorkOrderTicketVO.TicketView toTicketEntries(int ticketId, String workOrderKey) {
        WorkOrderTicketVO.Ticket ticket = WorkOrderTicketVO.Ticket.builder()
                .id(ticketId)
                .build();
        WorkOrderVO.WorkOrder workOrder = WorkOrderVO.WorkOrder.builder()
                .workOrderKey(workOrderKey)
                .build();
        WorkOrderTicketVO.TicketView ticketView = WorkOrderTicketVO.TicketView.builder()
                .ticket(ticket)
                .workOrder(workOrder)
                .build();
        ticketEntryPacker.wrap(ticketView);
        return ticketView;
    }

    /**
     * 转换工单至视图
     *
     * @param workOrderTicket
     * @return
     */
    public WorkOrderTicketVO.TicketView toTicketView(WorkOrderTicket workOrderTicket) {
        WorkOrderTicketVO.Ticket ticket = WorkOrderTicketVO.Ticket.builder()
                .id(workOrderTicket.getId())
                .userId(workOrderTicket.getUserId())
                .username(workOrderTicket.getUsername())
                .workOrderId(workOrderTicket.getWorkOrderId())
                .nodeId(workOrderTicket.getNodeId())
                .ticketPhase(workOrderTicket.getTicketPhase())
                .ticketStatus(workOrderTicket.getTicketStatus())
                .startTime(workOrderTicket.getStartTime())
                .endTime(workOrderTicket.getEndTime())
                .comment(workOrderTicket.getComment())
                .build();
        ticketApproveHelper.wrap(ticket);
        WorkOrderTicketVO.TicketView ticketView = WorkOrderTicketVO.TicketView.builder()
                .createUser(toCreateUser(workOrderTicket.getUsername()))
                .ticket(ticket)
                .build();
        workOrderPacker.wrap(ticketView);
        ticketEntryPacker.wrap(ticketView);
        // 工作流节点
        workOrderWorkflowPacker.wrap(ticketView);
        nodePacker.wrap(ticketView);
        return ticketView;
    }

    private UserVO.User toCreateUser(String username) {
        User user = userService.getByUsername(username);
        UserVO.User userVO = BeanCopierUtil.copyProperties(user, UserVO.User.class);
        userAvatarPacker.wrap(userVO);
        return userVO;
    }

}
