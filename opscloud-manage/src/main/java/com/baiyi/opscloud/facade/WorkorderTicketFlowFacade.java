package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicket;

/**
 * @Author baiyi
 * @Date 2020/5/6 4:50 下午
 * @Version 1.0
 */
public interface WorkorderTicketFlowFacade {

    void createTicketFlow(OcWorkorderTicket ocWorkorderTicket);
}
