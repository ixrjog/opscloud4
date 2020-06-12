package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.auth.RoleParam;
import com.baiyi.opscloud.domain.param.cloud.AliyunRAMPolicyParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessGroupParam;
import com.baiyi.opscloud.domain.param.workorder.WorkorderGroupParam;
import com.baiyi.opscloud.domain.param.workorder.WorkorderTicketParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderGroupVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderTicketEntryVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderTicketVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/26 1:13 下午
 * @Version 1.0
 */
public interface WorkorderFacade {

    DataTable<WorkorderGroupVO.WorkorderGroup> queryWorkorderGroupPage(WorkorderGroupParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> saveWorkorderGroup(WorkorderGroupVO.WorkorderGroup workorderGroup);

    List<WorkorderGroupVO.WorkorderGroup> queryWorkbenchWorkorderGroup();

    WorkorderTicketVO.Ticket createWorkorderTicket(WorkorderTicketParam.CreateTicket createTicket);

    WorkorderTicketVO.Ticket queryWorkorderTicket(WorkorderTicketParam.QueryTicket queryTicket);

    /**
     * 提交工单票据
     *
     * @param ticket
     * @return
     */
    BusinessWrapper<Boolean> submitWorkorderTicket(WorkorderTicketVO.Ticket ticket);

    BusinessWrapper<Boolean> agreeWorkorderTicket(int ticketId);

    BusinessWrapper<Boolean> disagreeWorkorderTicket(int ticketId);

    BusinessWrapper<Boolean> addTicketEntry(WorkorderTicketEntryVO.Entry entry);

    BusinessWrapper<Boolean> updateTicketEntry(WorkorderTicketEntryVO.Entry entry);

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

    List<WorkorderTicketEntryVO.Entry> queryUserTicketOcServerGroupByParam(ServerGroupParam.UserTicketOcServerGroupQuery queryParam);

    List<WorkorderTicketEntryVO.Entry> queryUserTicketOcUserGroupByParam(UserBusinessGroupParam.UserTicketOcUserGroupQuery queryParam);

    List<WorkorderTicketEntryVO.Entry> queryUserTicketOcAuthRoleByParam(RoleParam.UserTicketOcAuthRoleQuery queryParam);

    List<WorkorderTicketEntryVO.Entry> queryUserTicketRAMPolicyParam(AliyunRAMPolicyParam.UserTicketOcRamPolicyQuery queryParam);

    /**
     * 我的工单
     *
     * @param pageQuery
     * @return
     */
    DataTable<WorkorderTicketVO.Ticket> queryMyTicketPage(WorkorderTicketParam.QueryMyTicketPage pageQuery);

    DataTable<WorkorderTicketVO.Ticket> queryTicketPage(WorkorderTicketParam.QueryTicketPage pageQuery);

}
