package com.baiyi.opscloud.workorder.approve.impl;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrder;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketNode;
import com.baiyi.opscloud.service.workorder.WorkOrderService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketEntryService;
import com.baiyi.opscloud.workorder.approve.impl.base.AbstractApproveTicket;
import com.baiyi.opscloud.workorder.constants.ApprovalTypeConstants;
import com.baiyi.opscloud.workorder.constants.OrderPhaseCodeConstants;
import com.baiyi.opscloud.workorder.constants.ProcessStatusConstants;
import com.baiyi.opscloud.workorder.processor.ITicketProcessor;
import com.baiyi.opscloud.workorder.processor.factory.WorkOrderTicketProcessorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
            log.info("工单审批结束开始自动执行: ticketId = {} , createUser = {}", ticket.getId(), ticket.getCreateTime());
            ticket.setTicketPhase(OrderPhaseCodeConstants.PROCESSING.name());
            updateTicket(ticket, false);
            processing(ticket); // 开始执行
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
            ticket.setTicketPhase(OrderPhaseCodeConstants.FAILED.name());
        } else {
            // 成功
            ticket.setTicketPhase(OrderPhaseCodeConstants.SUCCESS.name());
        }
        updateTicket(ticket, true);
    }

    private void processing(WorkOrderTicket ticket) {
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        ITicketProcessor iTicketProcessor = WorkOrderTicketProcessorFactory.getByKey(workOrder.getWorkOrderKey());
        List<WorkOrderTicketEntry> entries = queryTicketEntries(ticket);
        entries.forEach(iTicketProcessor::process); // 执行所有工单条目
    }

    private List<WorkOrderTicketEntry> queryTicketEntries(WorkOrderTicket ticket) {
        return ticketEntryService.queryByWorkOrderTicketId(ticket.getId());
    }


}