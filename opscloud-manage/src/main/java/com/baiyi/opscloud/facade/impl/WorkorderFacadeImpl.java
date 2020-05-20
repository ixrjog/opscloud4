package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.builder.WorkorderTicketEntryBuilder;
import com.baiyi.opscloud.common.base.AccessLevel;
import com.baiyi.opscloud.common.base.TicketPhase;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.SessionUtils;
import com.baiyi.opscloud.decorator.DepartmentMemberDecorator;
import com.baiyi.opscloud.decorator.WorkorderGroupDecorator;
import com.baiyi.opscloud.decorator.WorkorderTicketDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.auth.RoleParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessGroupParam;
import com.baiyi.opscloud.domain.param.workorder.WorkorderGroupParam;
import com.baiyi.opscloud.domain.param.workorder.WorkorderTicketParam;
import com.baiyi.opscloud.domain.vo.org.OrgApprovalVO;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderGroupVO;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderTicketEntryVO;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderTicketVO;
import com.baiyi.opscloud.facade.UserFacade;
import com.baiyi.opscloud.facade.UserPermissionFacade;
import com.baiyi.opscloud.facade.WorkorderFacade;
import com.baiyi.opscloud.facade.WorkorderTicketFlowFacade;
import com.baiyi.opscloud.factory.ticket.ITicketHandler;
import com.baiyi.opscloud.factory.ticket.ITicketSubscribe;
import com.baiyi.opscloud.factory.ticket.WorkorderTicketFactory;
import com.baiyi.opscloud.factory.ticket.WorkorderTicketSubscribeFactory;
import com.baiyi.opscloud.service.auth.OcAuthRoleService;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketEntryService;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketFlowService;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketService;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketSubscribeService;
import com.baiyi.opscloud.service.user.OcUserGroupService;
import com.baiyi.opscloud.service.workorder.OcWorkorderGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/4/26 1:13 下午
 * @Version 1.0
 */
@Service
public class WorkorderFacadeImpl implements WorkorderFacade {

    @Resource
    private OcWorkorderGroupService ocWorkorderGroupService;

    @Resource
    private WorkorderGroupDecorator workorderGroupDecorator;

    @Resource
    private OcWorkorderTicketService ocWorkorderTicketService;

    @Resource
    private OcWorkorderTicketEntryService ocWorkorderTicketEntryService;

    @Resource
    private UserFacade userFacade;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private OcUserGroupService ocUserGroupService;

    @Resource
    private OcAuthRoleService ocAuthRoleService;

    @Resource
    private WorkorderTicketDecorator workorderTicketDecorator;

    @Resource
    private WorkorderTicketFlowFacade workorderTicketFlowFacade;

    @Resource
    private OcWorkorderTicketFlowService ocWorkorderTicketFlowService;
    @Resource
    private OcWorkorderTicketSubscribeService ocWorkorderTicketSubscribeService;

    @Resource
    private DepartmentMemberDecorator departmentMemberDecorator;

    public final static int AGREE_TYPE = 1;

    @Override
    public DataTable<OcWorkorderGroupVO.WorkorderGroup> queryWorkorderGroupPage(WorkorderGroupParam.PageQuery pageQuery) {
        DataTable<OcWorkorderGroup> table = ocWorkorderGroupService.queryOcWorkorderGroupByParam(pageQuery);
        List<OcWorkorderGroupVO.WorkorderGroup> page = BeanCopierUtils.copyListProperties(table.getData(), OcWorkorderGroupVO.WorkorderGroup.class);
        DataTable<OcWorkorderGroupVO.WorkorderGroup> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public List<OcWorkorderGroupVO.WorkorderGroup> queryWorkbenchWorkorderGroup() {
        List<OcWorkorderGroupVO.WorkorderGroup> list = BeanCopierUtils.copyListProperties(ocWorkorderGroupService.queryOcWorkorderGroupAll(), OcWorkorderGroupVO.WorkorderGroup.class);
        return list.stream().map(e -> workorderGroupDecorator.decorator(e)).collect(Collectors.toList());
    }

    @Override
    public OcWorkorderTicketVO.Ticket createWorkorderTicket(WorkorderTicketParam.CreateTicket createTicket) {
        ITicketHandler ticketHandler = WorkorderTicketFactory.getTicketHandlerByKey(createTicket.getWorkorderKey());
        return ticketHandler.createTicket(userFacade.getOcUserBySession());
    }

    @Override
    public OcWorkorderTicketVO.Ticket queryWorkorderTicket(WorkorderTicketParam.QueryTicket queryTicket) {
        OcWorkorderTicket ocWorkorderTicket = ocWorkorderTicketService.queryOcWorkorderTicketById(queryTicket.getId());
        OcWorkorderTicketVO.Ticket ticket = BeanCopierUtils.copyProperties(ocWorkorderTicket, OcWorkorderTicketVO.Ticket.class);
        return workorderTicketDecorator.decorator(ticket);
    }

    @Override
    public BusinessWrapper<Boolean> submitWorkorderTicket(OcWorkorderTicketVO.Ticket ticket) {
        OcWorkorderTicket ocWorkorderTicket = ocWorkorderTicketService.queryOcWorkorderTicketById(ticket.getId());
        // 校验用户
        if (!SessionUtils.getUsername().equals(ocWorkorderTicket.getUsername()))
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        // 校验状态
        if (!ocWorkorderTicket.getTicketPhase().equals(TicketPhase.CREATED.getPhase()))
            return new BusinessWrapper<>(ErrorEnum.WORKORDER_TICKET_PHASE_ERROR);
        // 校验工单条目
        if (ocWorkorderTicketEntryService.countOcWorkorderTicketEntryByTicketId(ticket.getId()) == 0)
            return new BusinessWrapper<>(ErrorEnum.WORKORDER_TICKET_ENTRIES_EXISTS);
        // 修改工单状态
        ocWorkorderTicket.setComment(ticket.getComment());
        ocWorkorderTicket.setTicketPhase(TicketPhase.APPLIED.getPhase());
        ocWorkorderTicketService.updateOcWorkorderTicket(ocWorkorderTicket);
        // 创建工单流程&发布订阅消息
        workorderTicketFlowFacade.createTicketFlow(ocWorkorderTicket);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> agreeWorkorderTicket(int ticketId) {
        return approvalWorkorderTicket(ticketId, AGREE_TYPE);
    }

    private BusinessWrapper<Boolean> approvalWorkorderTicket(int ticketId, int type) {
        OcUser ocUser = userFacade.getOcUserBySession();
        OcWorkorderTicket ocWorkorderTicket = ocWorkorderTicketService.queryOcWorkorderTicketById(ticketId);
        // 当前指针
        OcWorkorderTicketFlow ocWorkorderTicketFlow = ocWorkorderTicketFlowService.queryOcWorkorderTicketFlowById(ocWorkorderTicket.getFlowId());
        if (ocWorkorderTicketFlow.getFlowName().equals(TicketPhase.APPLIED.getPhase()))
            ocWorkorderTicketFlow = ocWorkorderTicketFlowService.queryOcWorkorderTicketFlowByflowParentId(ocWorkorderTicket.getFlowId());

        ITicketSubscribe ticketSubscribe = WorkorderTicketSubscribeFactory.getTicketSubscribeByKey(ocWorkorderTicketFlow.getFlowName());

        OcWorkorderTicketSubscribe ocWorkorderTicketSubscribe = ticketSubscribe.queryTicketSubscribe(ocWorkorderTicket, ocUser);
        if (ocWorkorderTicketSubscribe == null) {
            if (ocUser.getId() == ocWorkorderTicket.getUserId()) {
                OrgApprovalVO.OrgApproval orgApproval = departmentMemberDecorator.decorator(ocWorkorderTicket.getUserId());
                if (orgApproval.getIsApprovalAuthority()) {
                    ocWorkorderTicketFlow.setUserId(ocUser.getId());
                    ocWorkorderTicketFlow.setUsername(ocUser.getUsername());
                } else {
                    return new BusinessWrapper<>(ErrorEnum.WORKORDER_TICKET_NOT_THE_CURRENT_APPROVER);
                }
            } else {
                return new BusinessWrapper<>(ErrorEnum.WORKORDER_TICKET_NOT_THE_CURRENT_APPROVER);
            }
        } else {
            ocWorkorderTicketFlow.setUserId(ocWorkorderTicketSubscribe.getUserId());
            ocWorkorderTicketFlow.setUsername(ocWorkorderTicketSubscribe.getUsername());
        }
        // 开始审批
        ocWorkorderTicketFlow.setApprovalStatus(type);
        ocWorkorderTicketFlowService.updateOcWorkorderTicketFlow(ocWorkorderTicketFlow);
        // 更新工单状态
        if (type == AGREE_TYPE) {
            ocWorkorderTicket.setFlowId(ocWorkorderTicketFlowService.queryOcWorkorderTicketFlowByflowParentId(ocWorkorderTicketFlow.getId()).getId()); // 插入下级流程id
            ocWorkorderTicket.setTicketPhase(ocWorkorderTicketFlow.getFlowName());
            ocWorkorderTicketService.updateOcWorkorderTicket(ocWorkorderTicket);
            // 执行工单
            executorTicket(ocWorkorderTicket);
        } else {
            // 拒绝申请
            ocWorkorderTicketFlow = ocWorkorderTicketFlowService.queryOcWorkorderTicketByUniqueKey(ticketId, TicketPhase.FINALIZED.name());
            ocWorkorderTicket.setFlowId(ocWorkorderTicketFlow.getId());
            ocWorkorderTicket.setTicketPhase(TicketPhase.FINALIZED.name());
            ocWorkorderTicketService.updateOcWorkorderTicket(ocWorkorderTicket);
            ticketSubscribe.unsubscribe(ocWorkorderTicket); // 取消所有订阅
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> disagreeWorkorderTicket(int ticketId) {
        return approvalWorkorderTicket(ticketId, AGREE_TYPE - 1);
    }

    /**
     * 执行工单条目（会判断流程是否结束）
     *
     * @param ocWorkorderTicket
     */
    private void executorTicket(OcWorkorderTicket ocWorkorderTicket) {
        // 查询工作流
        OcWorkorderTicketFlow ocWorkorderTicketFlow = ocWorkorderTicketFlowService.queryOcWorkorderTicketFlowById(ocWorkorderTicket.getFlowId());
        if (!ocWorkorderTicketFlow.getFlowName().equals(TicketPhase.FINALIZED.getPhase())) return;
        ocWorkorderTicket.setFlowId(ocWorkorderTicketFlow.getId());
        ocWorkorderTicket.setTicketPhase(TicketPhase.FINALIZED.getPhase());
        ocWorkorderTicket.setTicketStatus(1); // 1 结束（成功）
        ocWorkorderTicket.setEndTime(new Date());
        ocWorkorderTicketService.updateOcWorkorderTicket(ocWorkorderTicket);

        List<OcWorkorderTicketEntry> entries = ocWorkorderTicketEntryService.queryOcWorkorderTicketEntryByTicketId(ocWorkorderTicket.getId());
        for (OcWorkorderTicketEntry entry : entries)
            WorkorderTicketFactory.getTicketHandlerByKey(entry.getEntryKey()).executorTicketEntry(entry);
    }

    @Override
    public BusinessWrapper<Boolean> addTicketEntry(OcWorkorderTicketEntryVO.Entry entry) {
        return WorkorderTicketFactory.getTicketHandlerByKey(entry.getEntryKey()).addTicketEntry(userFacade.getOcUserBySession(), entry);
    }

    @Override
    public BusinessWrapper<Boolean> updateTicketEntry(OcWorkorderTicketEntryVO.Entry entry) {
        return WorkorderTicketFactory.getTicketHandlerByKey(entry.getEntryKey()).updateTicketEntry(userFacade.getOcUserBySession(), entry);
    }

    @Override
    public BusinessWrapper<Boolean> delWorkorderTicketEntryById(int id) {
        OcWorkorderTicketEntry ocWorkorderTicketEntry = ocWorkorderTicketEntryService.queryOcWorkorderTicketEntryById(id);
        int ticketId = ocWorkorderTicketEntry.getWorkorderTicketId();
        // 需要鉴权
        OcWorkorderTicket ocWorkorderTicket = ocWorkorderTicketService.queryOcWorkorderTicketById(ticketId);
        // 校验用户
        if (!SessionUtils.getUsername().equals(ocWorkorderTicket.getUsername()))
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        // 校验状态
        if (!ocWorkorderTicket.getTicketPhase().equals(TicketPhase.CREATED.getPhase()))
            return new BusinessWrapper<>(ErrorEnum.WORKORDER_TICKET_PHASE_ERROR);
        ocWorkorderTicketEntryService.deleteOcWorkorderTicketEntryById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> delWorkorderTicketById(int id) {
        OcWorkorderTicket ocWorkorderTicket = ocWorkorderTicketService.queryOcWorkorderTicketById(id);
        // 校验用户
        BusinessWrapper<Boolean> wrapper = userPermissionFacade.checkAccessLevel(userFacade.getOcUserBySession(), AccessLevel.OPS.getLevel());
        if(!wrapper.isSuccess())
            return wrapper;
//        if (!SessionUtils.getUsername().equals(ocWorkorderTicket.getUsername()))
//            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        // 校验状态
        if (ocWorkorderTicket.getTicketPhase().equals(TicketPhase.FINALIZED.getPhase()))
            return new BusinessWrapper<>(ErrorEnum.WORKORDER_TICKET_PHASE_ERROR);
        try{
            // 删除工单条目
            ocWorkorderTicketEntryService.queryOcWorkorderTicketEntryByTicketId(id).forEach( e ->{
                ocWorkorderTicketEntryService.deleteOcWorkorderTicketEntryById(e.getId());
            });

            // 删除工单流程
            ocWorkorderTicketFlowService.queryOcWorkorderTicketByTicketId(id).forEach( e ->{
                ocWorkorderTicketFlowService.deleteOcWorkorderTicketFlowById(e.getId());
            });

            // 删除工单订阅
            ocWorkorderTicketSubscribeService.queryOcWorkorderTicketSubscribeByTicketId(id).forEach( e ->{
                ocWorkorderTicketSubscribeService.deleteOcWorkorderTicketSubscribeById(e.getId());
            });
            // 删除工单
            ocWorkorderTicketService.deleteOcWorkorderTicketById(id);
        }catch (Exception e){
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public List<OcWorkorderTicketEntryVO.Entry> queryUserTicketOcServerGroupByParam(ServerGroupParam.UserTicketOcServerGroupQuery queryParam) {
        List<OcServerGroup> list = ocServerGroupService.queryUserTicketOcServerGroupByParam(queryParam);
        return list.stream().map(e ->
                WorkorderTicketEntryBuilder.build(queryParam.getWorkorderTicketId(), e)
        ).collect(Collectors.toList());
    }

    @Override
    public List<OcWorkorderTicketEntryVO.Entry> queryUserTicketOcUserGroupByParam(UserBusinessGroupParam.UserTicketOcUserGroupQuery queryParam) {
        OcUser ocUser = userFacade.getOcUserBySession();
        queryParam.setUserId(ocUser.getId());

        List<OcUserGroup> list = ocUserGroupService.queryUserTicketOcUserGroupByParam(queryParam);
        return list.stream().map(e ->
                WorkorderTicketEntryBuilder.build(queryParam.getWorkorderTicketId(), e)
        ).collect(Collectors.toList());
    }

    @Override
    public List<OcWorkorderTicketEntryVO.Entry> queryUserTicketOcAuthRoleByParam(RoleParam.UserTicketOcAuthRoleQuery queryParam) {
        OcUser ocUser = userFacade.getOcUserBySession();
        queryParam.setUsername(ocUser.getUsername());
        List<OcAuthRole> list = ocAuthRoleService.queryUserTicketOcAuthRoleByParam(queryParam);
        return list.stream().map(e ->
                WorkorderTicketEntryBuilder.build(queryParam.getWorkorderTicketId(), e)
        ).collect(Collectors.toList());
    }

    @Override
    public DataTable<OcWorkorderTicketVO.Ticket> queryMyTicketPage(WorkorderTicketParam.QueryMyTicketPage queryPage) {
        OcUser ocUser = userFacade.getOcUserBySession();
        queryPage.setUserId(ocUser.getId());
        DataTable<OcWorkorderTicket> table = ocWorkorderTicketService.queryMyOcWorkorderTicketByParam(queryPage);
        return getTicketDataTable(table);
    }

    @Override
    public DataTable<OcWorkorderTicketVO.Ticket> queryTicketPage(WorkorderTicketParam.QueryTicketPage pageQuery) {
        DataTable<OcWorkorderTicket> table = ocWorkorderTicketService.queryOcWorkorderTicketByParam(pageQuery);
        return getTicketDataTable(table);
    }

    private DataTable<OcWorkorderTicketVO.Ticket> getTicketDataTable(DataTable<OcWorkorderTicket> table) {
        List<OcWorkorderTicketVO.Ticket> page = BeanCopierUtils.copyListProperties(table.getData(), OcWorkorderTicketVO.Ticket.class);
        DataTable<OcWorkorderTicketVO.Ticket> dataTable = new DataTable<>(page.stream().map(e -> workorderTicketDecorator.decorator(e)
        ).collect(Collectors.toList()), table.getTotalNum());
        return dataTable;
    }

}
