package com.baiyi.opscloud.service.ticket;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicket;
import com.baiyi.opscloud.domain.param.workorder.WorkorderTicketParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/27 5:30 下午
 * @Version 1.0
 */
public interface OcWorkorderTicketService {

    OcWorkorderTicket queryOcWorkorderTicketById(int id);

    List<OcWorkorderTicket> queryOcWorkorderTicketByParam(OcWorkorderTicket ocWorkorderTicket);

    void addOcWorkorderTicket(OcWorkorderTicket ocWorkorderTicket);

    void updateOcWorkorderTicket(OcWorkorderTicket ocWorkorderTicket);

    void deleteOcWorkorderTicketById(int id);

    DataTable<OcWorkorderTicket> queryMyOcWorkorderTicketByParam(WorkorderTicketParam.QueryMyTicketPage pageQuery);

    DataTable<OcWorkorderTicket> queryOcWorkorderTicketByParam(WorkorderTicketParam.QueryTicketPage pageQuery);
}
