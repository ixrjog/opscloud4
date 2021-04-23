package com.baiyi.opscloud.factory.ticket;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketSubscribe;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderTicketVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/6 1:28 下午
 * @Version 1.0
 */
public interface ITicketSubscribe {

    String getKey();

    OcWorkorderTicketSubscribe queryTicketSubscribe(OcWorkorderTicket ocWorkorderTicket, OcUser ocUser);

    /**
     * 订阅工单
     *
     * @param ocWorkorderTicket
     * @return
     */
    BusinessWrapper<Boolean> subscribe(OcWorkorderTicket ocWorkorderTicket);

    /**
     * 取消订阅
     *
     * @param ocWorkorderTicket
     * @return
     */
    void unsubscribe(OcWorkorderTicket ocWorkorderTicket);

    void invokeFlowStep(WorkorderTicketVO.Ticket ticket, String ticketPhase);

    /**
     * 当前步骤用户是否可审批
     *
     * @param ocUser
     * @param ticket
     * @return
     */
    Boolean isAllowApproval(OcUser ocUser, WorkorderTicketVO.Ticket ticket);

    List<OcWorkorderTicketSubscribe> queryTicketSubscribes(WorkorderTicketVO.Ticket ticket);

    void sendTicketFlowMsg(OcWorkorderTicket ocWorkorderTicket);
}
