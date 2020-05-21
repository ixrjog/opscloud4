package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.auth.RoleParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessGroupParam;
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

    BusinessWrapper<Boolean> saveWorkorderGroup(OcWorkorderGroupVO.WorkorderGroup workorderGroup);

    List<OcWorkorderGroupVO.WorkorderGroup> queryWorkbenchWorkorderGroup();

    // WorkorderTicketParam.CreateTicket createTicket

    OcWorkorderTicketVO.Ticket createWorkorderTicket(WorkorderTicketParam.CreateTicket createTicket);

    OcWorkorderTicketVO.Ticket queryWorkorderTicket(WorkorderTicketParam.QueryTicket queryTicket);

    /**
     * 提交工单票据
     *
     * @param ticket
     * @return
     */
    BusinessWrapper<Boolean> submitWorkorderTicket(OcWorkorderTicketVO.Ticket ticket);

    BusinessWrapper<Boolean> agreeWorkorderTicket(int ticketId);

    BusinessWrapper<Boolean> disagreeWorkorderTicket(int ticketId);

    BusinessWrapper<Boolean> addTicketEntry(OcWorkorderTicketEntryVO.Entry entry);

    BusinessWrapper<Boolean> updateTicketEntry(OcWorkorderTicketEntryVO.Entry entry);

    /**
     * 删除工单条目
     *
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> delWorkorderTicketEntryById(int id);

    /**
     * 删除工单
     *
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> delWorkorderTicketById(int id);

    List<OcWorkorderTicketEntryVO.Entry> queryUserTicketOcServerGroupByParam(ServerGroupParam.UserTicketOcServerGroupQuery queryParam);

    List<OcWorkorderTicketEntryVO.Entry> queryUserTicketOcUserGroupByParam(UserBusinessGroupParam.UserTicketOcUserGroupQuery queryParam);

    List<OcWorkorderTicketEntryVO.Entry> queryUserTicketOcAuthRoleByParam(RoleParam.UserTicketOcAuthRoleQuery queryParam);

    /**
     * 我的工单
     *
     * @param pageQuery
     * @return
     */
    DataTable<OcWorkorderTicketVO.Ticket> queryMyTicketPage(WorkorderTicketParam.QueryMyTicketPage pageQuery);

    DataTable<OcWorkorderTicketVO.Ticket> queryTicketPage(WorkorderTicketParam.QueryTicketPage pageQuery);

}
