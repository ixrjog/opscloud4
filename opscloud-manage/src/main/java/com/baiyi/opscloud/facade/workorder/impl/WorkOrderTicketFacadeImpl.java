package com.baiyi.opscloud.facade.workorder.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.facade.workorder.WorkOrderTicketFacade;
import com.baiyi.opscloud.packer.workorder.WorkOrderTicketPacker;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketEntryService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketService;
import com.baiyi.opscloud.workorder.constants.OrderPhaseCodeConstants;
import com.baiyi.opscloud.workorder.processor.ITicketProcessor;
import com.baiyi.opscloud.workorder.processor.factory.WorkOrderTicketProcessorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 工单票据外观设计
 * @Author baiyi
 * @Date 2022/1/11 2:13 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class WorkOrderTicketFacadeImpl implements WorkOrderTicketFacade {

    private final WorkOrderTicketPacker workOrderTicketPacker;

    private final WorkOrderTicketService workOrderTicketService;

    private final WorkOrderTicketEntryService workOrderTicketEntryService;

    public void createTicket(){

    }

    public void approveTicket(){

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
