package com.baiyi.opscloud.workorder.approve.impl;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketNode;
import com.baiyi.opscloud.workorder.approve.impl.base.AbstractApproveTicket;
import com.baiyi.opscloud.workorder.constants.ApprovalTypeConstants;
import com.baiyi.opscloud.workorder.constants.OrderPhaseCodeConstants;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/1/19 3:23 PM
 * @Version 1.0
 */
@Component
public class CancelApproveTicket extends AbstractApproveTicket {

    @Override
    public String getApprovalType() {
        return ApprovalTypeConstants.CANCEL.name();
    }

    @Override
    protected void postHandle(WorkOrderTicket ticket, WorkOrderTicketNode ticketNode) {
        ticket.setTicketPhase(OrderPhaseCodeConstants.CLOSED.name());
        updateTicket(ticket, true);
    }

}
