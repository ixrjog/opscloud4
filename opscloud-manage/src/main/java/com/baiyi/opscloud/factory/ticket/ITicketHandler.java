package com.baiyi.opscloud.factory.ticket;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketEntry;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderTicketEntryVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderTicketVO;

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

    BusinessWrapper<Boolean> addTicketEntry(OcUser ocUser,  WorkorderTicketEntryVO.Entry entry);

    BusinessWrapper<Boolean> updateTicketEntry(OcUser ocUser,  WorkorderTicketEntryVO.Entry entry);

    WorkorderTicketEntryVO.Entry convertTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry);

    WorkorderTicketVO.Ticket createTicket(OcUser ocUser);

    String getKey();

}
