package com.baiyi.opscloud.facade.workorder.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.common.util.WorkflowUtil;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkflowVO;
import com.baiyi.opscloud.facade.workorder.WorkOrderTicketFacade;
import com.baiyi.opscloud.facade.workorder.WorkOrderTicketNodeFacade;
import com.baiyi.opscloud.packer.workorder.WorkOrderTicketPacker;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.service.workorder.WorkOrderService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketEntryService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketNodeService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketService;
import com.baiyi.opscloud.workorder.constants.NodeTypeConstants;
import com.baiyi.opscloud.workorder.constants.OrderPhaseCodeConstants;
import com.baiyi.opscloud.workorder.exception.TicketCommonException;
import com.baiyi.opscloud.workorder.processor.ITicketProcessor;
import com.baiyi.opscloud.workorder.processor.factory.WorkOrderTicketProcessorFactory;
import com.baiyi.opscloud.workorder.query.factory.WorkOrderTicketEntryQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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

    private final WorkOrderTicketNodeFacade workOrderTicketNodeFacade;

    private final WorkOrderTicketNodeService workOrderTicketNodeService;

    @Override
    public WorkOrderTicketVO.TicketView createTicket(WorkOrderTicketParam.CreateTicket createTicket) {
        final String username = SessionUtil.getUsername();
        WorkOrderTicket workOrderTicket = workOrderTicketService.getNewTicketByUser(createTicket.getWorkOrderKey(), username);
        WorkOrder workOrder = workOrderService.getByKey(createTicket.getWorkOrderKey());
        if (workOrderTicket == null) {
            workOrderTicket = createNewTicket(workOrder, username);
        }
        return toTicketView(workOrderTicket);
    }

    @Override
    public WorkOrderTicketVO.TicketView saveTicket(WorkOrderTicketParam.SaveTicket saveTicket) {
        WorkOrderTicket workOrderTicket = workOrderTicketService.getById(saveTicket.getTicketId());
        preSaveHandle(workOrderTicket, saveTicket);
        return toTicketView(workOrderTicket);
    }

    @Override
    public WorkOrderTicketVO.TicketView submitTicket(WorkOrderTicketParam.SubmitTicket submitTicket) {
        WorkOrderTicket workOrderTicket = workOrderTicketService.getById(submitTicket.getTicketId());
        preSaveHandle(workOrderTicket, submitTicket);
        // 验证工单完整性
        verifyTicket(workOrderTicket);
        // 开始执行
        return toTicketView(workOrderTicket);
    }

    // 验证工单完整性
    private void verifyTicket(WorkOrderTicket workOrderTicket) {
        if (workOrderTicketEntryService.countByWorkOrderTicketId(workOrderTicket.getId()) == 0)
            throw new TicketCommonException("工单选项(条目)未配置");
        WorkOrder workOrder = workOrderService.getById(workOrderTicket.getWorkOrderId());
        // 验证工作流节审批配置
        workOrderTicketNodeFacade.verifyWorkflowNodes(workOrder,workOrderTicket);
    }

    private void preSaveHandle(WorkOrderTicket workOrderTicket, WorkOrderTicketParam.SaveTicket saveTicket) {
        if (workOrderTicket == null)
            throw new TicketCommonException("工单不存在！");
        final String username = SessionUtil.getUsername();
        if (!workOrderTicket.getUsername().equals(username))
            throw new TicketCommonException("只有本人才能保存工单！");
        saveTicketComment(workOrderTicket, saveTicket);
        WorkOrder workOrder = workOrderService.getById(workOrderTicket.getWorkOrderId());
        Map<String, WorkflowVO.Node> originalWorkflowNodeMap = WorkflowUtil.toWorkflowNodeMap(workOrder.getWorkflow());
        saveTicket.getWorkflowView().getNodes().forEach(node -> {
            if (NodeTypeConstants.USER_LIST.getCode() == node.getType() && node.getAuditUser() != null) {
                // 更新用户指定审批人
                workOrderTicketNodeFacade.updateWorkflowNodeAuditUser(saveTicket.getTicketId(), node.getName(), node.getAuditUser());
            }
        });
    }

    private void saveTicketComment(WorkOrderTicket workOrderTicket, WorkOrderTicketParam.SaveTicket saveTicket) {
        if (StringUtils.isEmpty(saveTicket.getComment())) {
            if (StringUtils.isEmpty(workOrderTicket.getComment()))
                return;
        } else {
            if (saveTicket.getComment().equals(workOrderTicket.getComment()))
                return;
        }
        workOrderTicket.setComment(saveTicket.getComment());
        workOrderTicketService.update(workOrderTicket);
    }

    /**
     * 创建新工单
     *
     * @param workOrder
     * @param username
     * @return
     */
    private WorkOrderTicket createNewTicket(WorkOrder workOrder, String username) {
        User user = userService.getByUsername(username);
        WorkOrderTicket workOrderTicket = WorkOrderTicket.builder()
                .username(username)
                .userId(user.getId())
                .workOrderId(workOrder.getId())
                .ticketPhase(OrderPhaseCodeConstants.NEW.name())
                .ticketStatus(0)
                .build();
        workOrderTicketService.add(workOrderTicket);
        workOrderTicketNodeFacade.createWorkflowNodes(workOrder, workOrderTicket);
        // 更新节点ID
        WorkOrderTicketNode workOrderTicketNode = workOrderTicketNodeService.getByUniqueKey(workOrderTicket.getId(),0);
        workOrderTicket.setNodeId(workOrderTicketNode.getId());
        workOrderTicketService.update( workOrderTicket);

        return workOrderTicket;
    }

    @Override
    public WorkOrderTicketVO.TicketView getTicket(Integer ticketId) {
        WorkOrderTicket workOrderTicket = workOrderTicketService.getById(ticketId);
        return toTicketView(workOrderTicket);
    }

    @Override
    public List<WorkOrderTicketVO.Entry> queryTicketEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        WorkOrderTicket ticket = workOrderTicketService.getById(entryQuery.getWorkOrderTicketId());
        if (ticket == null)
            throw new TicketCommonException("工单票据不存在！");
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        return WorkOrderTicketEntryQueryFactory.getByKey(workOrder.getWorkOrderKey()).query(entryQuery);
    }

    public void approveTicket() {

    }

    @Override
    public WorkOrderTicketVO.TicketView updateTicketEntry(WorkOrderTicketEntryParam.TicketEntry ticketEntry) {
        WorkOrderTicket workOrderTicket = workOrderTicketService.getById(ticketEntry.getWorkOrderTicketId());
        return toTicketView(workOrderTicket);
    }

    @Override
    public void addTicketEntry(WorkOrderTicketEntryParam.TicketEntry ticketEntry) {
        WorkOrderTicket workOrderTicket = workOrderTicketService.getById(ticketEntry.getWorkOrderTicketId());
        if (!workOrderTicket.getUsername().equals(SessionUtil.getUsername()))
            throw new TicketCommonException("不合法的请求: 只有工单创建人才能新增条目！");
        WorkOrder workOrder = workOrderService.getById(workOrderTicket.getWorkOrderId());
        ITicketProcessor iTicketProcessor = WorkOrderTicketProcessorFactory.getByKey(workOrder.getWorkOrderKey());
        if (iTicketProcessor == null)
            throw new TicketCommonException("工单类型不正确！");
        WorkOrderTicketEntry verificationTicketEntry = BeanCopierUtil.copyProperties(ticketEntry, WorkOrderTicketEntry.class);
        iTicketProcessor.verify(verificationTicketEntry); // 验证
        workOrderTicketEntryService.add(verificationTicketEntry); // 新增
    }

    @Override
    public void deleteTicketEntry(Integer ticketEntryId) {
        WorkOrderTicketEntry ticketEntry = workOrderTicketEntryService.getById(ticketEntryId);
        if (ticketEntry == null)
            throw new TicketCommonException("工单条目不存在！");
        WorkOrderTicket workOrderTicket = workOrderTicketService.getById(ticketEntry.getWorkOrderTicketId());
        if (!OrderPhaseCodeConstants.NEW.name().equals(workOrderTicket.getTicketPhase()))
            throw new TicketCommonException("只有新建工单才能修改或删除条目！");
        workOrderTicketEntryService.deleteById(ticketEntryId);
    }

    @Override
    public WorkOrderTicketVO.TicketView toTicketView(WorkOrderTicket workOrderTicket) {
        return workOrderTicketPacker.toTicketView(workOrderTicket);
    }

}
