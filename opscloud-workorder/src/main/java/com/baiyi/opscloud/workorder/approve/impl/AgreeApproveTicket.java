package com.baiyi.opscloud.workorder.approve.impl;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrder;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketNode;
import com.baiyi.opscloud.service.workorder.WorkOrderService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketEntryService;
import com.baiyi.opscloud.workorder.approve.impl.base.AbstractApproveTicket;
import com.baiyi.opscloud.workorder.constants.ApprovalTypeConstants;
import com.baiyi.opscloud.workorder.constants.OrderTicketPhaseCodeConstants;
import com.baiyi.opscloud.workorder.constants.OrderTicketStatusConstants;
import com.baiyi.opscloud.workorder.constants.ProcessStatusConstants;
import com.baiyi.opscloud.workorder.processor.ITicketProcessor;
import com.baiyi.opscloud.workorder.processor.factory.WorkOrderTicketProcessorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/1/19 3:03 PM
 * @Version 1.0
 */
@Slf4j
@Component
public class AgreeApproveTicket extends AbstractApproveTicket {

    @Resource
    private WorkOrderService workOrderService;

    @Resource
    private WorkOrderTicketEntryService ticketEntryService;

    @Override
    public String getApprovalType() {
        return ApprovalTypeConstants.AGREE.name();
    }

    @Override
    protected void postHandle(WorkOrderTicket ticket, WorkOrderTicketNode ticketNode) {
        // 尝试移动审批节点指针
        WorkOrderTicketNode nextNode = ticketNodeService.getByUniqueKey(ticket.getId(), ticketNode.getId());
        if (nextNode == null) {
            log.info("执行工单: ticketId={}, createUser={}", ticket.getId(), ticket.getUsername());
            ticket.setTicketPhase(OrderTicketPhaseCodeConstants.PROCESSING.name());
            updateTicket(ticket, false);
            processing(ticket);
            statisticalResults(ticket);
        } else {
            // 移动审批节点指针
            ticket.setNodeId(nextNode.getId());
            updateTicket(ticket, false);
        }
    }

    private void statisticalResults(WorkOrderTicket ticket) {
        List<WorkOrderTicketEntry> entries = queryTicketEntries(ticket);
        Optional<WorkOrderTicketEntry> entryOptional = entries.stream().filter(entry -> entry.getEntryStatus() == ProcessStatusConstants.FAILED.getStatus()).findFirst();
        if (entryOptional.isPresent()) {
            // 失败
            ticket.setTicketPhase(OrderTicketPhaseCodeConstants.FAILED.name());
            ticket.setTicketStatus(OrderTicketStatusConstants.FAILED.getStatus());
        } else {
            // 成功
            ticket.setTicketPhase(OrderTicketPhaseCodeConstants.SUCCESS.name());
            ticket.setTicketStatus(OrderTicketStatusConstants.SUCCESS.getStatus());
        }
        updateTicket(ticket, true);
    }

    private void processing(WorkOrderTicket ticket) {
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        ITicketProcessor<?> iTicketProcessor = WorkOrderTicketProcessorFactory.getByKey(workOrder.getWorkOrderKey());
        List<WorkOrderTicketEntry> entries = queryTicketEntries(ticket);
        // 执行所有工单条目
        entries.forEach(iTicketProcessor::process);
    }

    private List<WorkOrderTicketEntry> queryTicketEntries(WorkOrderTicket ticket) {
        return ticketEntryService.queryByWorkOrderTicketId(ticket.getId());
    }

}