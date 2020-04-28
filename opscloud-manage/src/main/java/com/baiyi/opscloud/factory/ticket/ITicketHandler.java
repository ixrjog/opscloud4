package com.baiyi.opscloud.factory.ticket;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketEntry;
import com.baiyi.opscloud.domain.param.workorder.WorkorderTicketEntryParam;

/**
 * @Author baiyi
 * @Date 2020/4/27 2:59 下午
 * @Version 1.0
 */
public interface ITicketHandler {

    /**
     * 执行工单条目
     **/
    void executorTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry);

    BusinessWrapper<Boolean> addTicketEntry(OcUser ocUser, WorkorderTicketEntryParam.TicketEntry ticketEntry);

    BusinessWrapper<Boolean> updateTicketEntry(OcUser ocUser, WorkorderTicketEntryParam.TicketEntry ticketEntry);

    void createTicket(OcUser ocUser);

    String getKey();

//    /**
//     * 审批通过
//     *
//     * @param ticketId
//     * @return
//     */
//    BusinessWrapper<Boolean> approvalTicket(int ticketId);
//
//    /**
//     * 审批拒绝
//     * @param ticketId
//     * @return
//     */
//    BusinessWrapper<Boolean> disapproveTicket(int ticketId);
//
//    BusinessWrapper<Boolean> createTicket();

}
