package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.workorder.WorkorderGroupParam;
import com.baiyi.opscloud.domain.param.workorder.WorkorderTicketParam;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderGroupVO;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderTicketEntryVO;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderTicketVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/26 1:13 下午
 * @Version 1.0
 */
public interface WorkorderFacade {

    DataTable<OcWorkorderGroupVO.WorkorderGroup> queryWorkorderGroupPage(WorkorderGroupParam.PageQuery pageQuery);

    List<OcWorkorderGroupVO.WorkorderGroup> queryWorkbenchWorkorderGroup();

    // WorkorderTicketParam.CreateTicket createTicket

    OcWorkorderTicketVO.Ticket createWorkorderTicket(WorkorderTicketParam.CreateTicket createTicket);

    OcWorkorderTicketVO.Ticket queryWorkorderTicket(WorkorderTicketParam.QueryTicket queryTicket);

    /**
     * 提交工单票据
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> submitWorkorderTicket(int id);

    BusinessWrapper<Boolean> addTicketEntry(OcWorkorderTicketEntryVO.Entry entry);

    BusinessWrapper<Boolean> updateTicketEntry(OcWorkorderTicketEntryVO.Entry entry);

    BusinessWrapper<Boolean> delWorkorderTicketEntryById(int id);

    List<OcWorkorderTicketEntryVO.Entry> queryUserTicketOcServerGroupByParam(ServerGroupParam.UserTicketOcServerGroupQuery queryParam);

}
