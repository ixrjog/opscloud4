package com.baiyi.opscloud.facade.workorder.impl;

import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrder;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.facade.workorder.WorkOrderTicketFacade;
import com.baiyi.opscloud.packer.workorder.WorkOrderTicketPacker;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.service.workorder.WorkOrderService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketEntryService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketService;
import com.baiyi.opscloud.workorder.constants.OrderPhaseCodeConstants;
import com.baiyi.opscloud.workorder.processor.ITicketProcessor;
import com.baiyi.opscloud.workorder.processor.factory.WorkOrderTicketProcessorFactory;
import com.baiyi.opscloud.workorder.query.factory.WorkOrderTicketEntryQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 工单票据外观设计
 *
 * @Author baiyi
 * @Date 2022/1/11 2:13 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class WorkOrderTicketFacadeImpl implements WorkOrderTicketFacade {

    private final WorkOrderService workOrderService;

    private final WorkOrderTicketPacker workOrderTicketPacker;

    private final WorkOrderTicketService workOrderTicketService;

    private final WorkOrderTicketEntryService workOrderTicketEntryService;

    private final UserService userService;

    @Override
    public WorkOrderTicketVO.TicketView createWorkOrderTicket(WorkOrderTicketParam.CreateTicket createTicket) {
        final String username = SessionUtil.getUsername();
        WorkOrderTicket workOrderTicket = workOrderTicketService.getNewTicketByUser(createTicket.getWorkOrderKey(), username);
        WorkOrder workOrder = workOrderService.getByKey(createTicket.getWorkOrderKey());
        if (workOrderTicket == null) {
            User user = userService.getByUsername(username);
            workOrderTicket = WorkOrderTicket.builder()
                    .username(username)
                    .userId(user.getId())
                    .workOrderId(workOrder.getId())
                    .ticketPhase(OrderPhaseCodeConstants.NEW.name())
                    .ticketStatus(0)
                    .build();
            workOrderTicketService.add(workOrderTicket);
        }
        return toTicketView(workOrderTicket);
    }

    @Override
    public List<WorkOrderTicketVO.Entry> queryWorkOrderTicketEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        WorkOrderTicket ticket = workOrderTicketService.getById(entryQuery.getWorkOrderTicketId());
        if (ticket == null)
            throw new CommonRuntimeException("工单票据不存在！");
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        return WorkOrderTicketEntryQueryFactory.getByKey(workOrder.getWorkOrderKey()).query(entryQuery);
    }

    public void approveTicket() {

    }

    @Override
    public WorkOrderTicketVO.TicketView updateTicketEntry(WorkOrderTicketParam.TicketEntry ticketEntry) {
        WorkOrderTicket workOrderTicket = workOrderTicketService.getById(ticketEntry.getWorkOrderTicketId());
        return toTicketView(workOrderTicket);
    }

    @Override
    public WorkOrderTicketVO.TicketView addTicketEntry(WorkOrderTicketParam.TicketEntry ticketEntry) {
        WorkOrderTicket workOrderTicket = workOrderTicketService.getById(ticketEntry.getWorkOrderTicketId());
        if (!workOrderTicket.getUsername().equals(SessionUtil.getUsername()))
            throw new RuntimeException("不合法的请求: 只有工单创建人才能新增条目！");
        ITicketProcessor iTicketProcessor = WorkOrderTicketProcessorFactory.getByKey(ticketEntry.getEntryKey());
        WorkOrderTicketEntry verificationTicketEntry = BeanCopierUtil.copyProperties(ticketEntry, WorkOrderTicketEntry.class);
        iTicketProcessor.verify(verificationTicketEntry);
        return toTicketView(workOrderTicket);
    }

    @Override
    public WorkOrderTicketVO.TicketView deleteTicketEntry(Integer ticketEntryId) {
        WorkOrderTicketEntry ticketEntry = workOrderTicketEntryService.getById(ticketEntryId);
        if (ticketEntry == null)
            throw new RuntimeException("工单条目不存在！");
        WorkOrderTicket workOrderTicket = workOrderTicketService.getById(ticketEntry.getWorkOrderTicketId());
        if (!OrderPhaseCodeConstants.NEW.name().equals(workOrderTicket.getTicketPhase()))
            throw new RuntimeException("只有新建工单才能修改或删除条目！");
        workOrderTicketEntryService.deleteById(ticketEntryId);
        return toTicketView(workOrderTicket);
    }

    private WorkOrderTicketVO.TicketView toTicketView(WorkOrderTicket workOrderTicket) {
        return workOrderTicketPacker.toTicketView(workOrderTicket);
    }

}
