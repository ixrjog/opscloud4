package com.baiyi.opscloud.factory.ticket;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketSubscribe;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderTicketVO;

/**
 * @Author baiyi
 * @Date 2020/5/6 1:28 下午
 * @Version 1.0
 */
public interface ITicketSubscribe {

    String getKey();

    OcWorkorderTicketSubscribe queryTicketSubscribe(OcWorkorderTicket ocWorkorderTicket, OcUser ocUser);

    BusinessWrapper<Boolean> subscribe(OcWorkorderTicket ocWorkorderTicket);

    void invokeFlowStep(OcWorkorderTicketVO.Ticket ticket, String ticketPhase);

}
