package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.builder.WorkorderTicketEntryBuilder;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.decorator.WorkorderGroupDecorator;
import com.baiyi.opscloud.decorator.WorkorderTicketDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.workorder.WorkorderGroupParam;
import com.baiyi.opscloud.domain.param.workorder.WorkorderTicketParam;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderGroupVO;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderTicketEntryVO;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderTicketVO;
import com.baiyi.opscloud.facade.UserFacade;
import com.baiyi.opscloud.facade.WorkorderFacade;
import com.baiyi.opscloud.factory.ticket.ITicketHandler;
import com.baiyi.opscloud.factory.ticket.WorkorderTicketFactory;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketEntryService;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketService;
import com.baiyi.opscloud.service.workorder.OcWorkorderGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    private OcServerGroupService ocServerGroupService;

    @Resource
    private WorkorderTicketDecorator workorderTicketDecorator;

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
    public BusinessWrapper<Boolean> submitWorkorderTicket(int id) {

        return BusinessWrapper.SUCCESS;
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
        // TODO 需要鉴权
        OcWorkorderTicketEntry ocWorkorderTicketEntry = ocWorkorderTicketEntryService.queryOcWorkorderTicketEntryById(id);
        ocWorkorderTicketEntryService.deleteOcWorkorderTicketEntryById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public List<OcWorkorderTicketEntryVO.Entry> queryUserTicketOcServerGroupByParam(ServerGroupParam.UserTicketOcServerGroupQuery queryParam) {
        OcUser ocUser = userFacade.getOcUserBySession();
        queryParam.setUserId(ocUser.getId());
        List<OcServerGroup> list = ocServerGroupService.queryUserTicketOcServerGroupByParam(queryParam);
        return list.stream().map(e ->
                WorkorderTicketEntryBuilder.build(queryParam.getWorkorderTicketId(), e)
        ).collect(Collectors.toList());
    }


}
