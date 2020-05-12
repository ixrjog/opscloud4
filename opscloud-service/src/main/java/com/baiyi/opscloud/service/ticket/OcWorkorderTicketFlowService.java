package com.baiyi.opscloud.service.ticket;

import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketFlow;

/**
 * @Author baiyi
 * @Date 2020/5/6 4:25 下午
 * @Version 1.0
 */
public interface OcWorkorderTicketFlowService {

    OcWorkorderTicketFlow queryOcWorkorderTicketFlowById(int id);

    OcWorkorderTicketFlow queryOcWorkorderTicketFlowByflowParentId(int flowParentId);

    void addOcWorkorderTicketFlow(OcWorkorderTicketFlow ocWorkorderTicketFlow);

    void updateOcWorkorderTicketFlow(OcWorkorderTicketFlow ocWorkorderTicketFlow);
}
