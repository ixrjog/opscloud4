package com.baiyi.opscloud.workorder.helper.strategy.impl;

import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrder;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.notice.INoticeMessage;
import com.baiyi.opscloud.workorder.constants.OrderTicketPhaseCodeConstants;
import com.baiyi.opscloud.workorder.helper.strategy.base.AbstractSendNotice;
import com.baiyi.opscloud.workorder.model.TicketNoticeModel;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

import static com.baiyi.opscloud.datasource.manager.base.NoticeManager.MsgKeys.TICKET_END;

/**
 * @Author baiyi
 * @Date 2022/1/27 4:51 PM
 * @Version 1.0
 */
@Component
public class SendEndNotice extends AbstractSendNotice {

    @Override
    public void send(WorkOrderTicket ticket) {
        User user = userService.getByUsername(ticket.getUsername());
        send(ticket.getId(), Lists.newArrayList(user), TICKET_END, buildNoticeMessage(ticket));
    }

    protected void send(Integer ticketId, List<User> users, String msgKey, INoticeMessage noticeMessage) {
        if (CollectionUtils.isEmpty(users)) {
            return;
        }
        users.forEach(user ->
            noticeManager.sendMessage(user, msgKey, noticeMessage)
        );
    }

    @Override
    public Set<String> getPhases() {
        return Sets.newHashSet(OrderTicketPhaseCodeConstants.SUCCESS.getPhase(),
                OrderTicketPhaseCodeConstants.FAILED.getPhase(),
                OrderTicketPhaseCodeConstants.REJECT.getPhase());
    }

    @Override
    protected INoticeMessage buildNoticeMessage(WorkOrderTicket ticket) {
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        return TicketNoticeModel.EndNoticeMessage.builder()
                .ticketId(ticket.getId())
                .workOrderName(workOrder.getName())
                .result(OrderTicketPhaseCodeConstants.getEnum(ticket.getTicketPhase()).getResult())
                .build();
    }

}