package com.baiyi.opscloud.facade.workorder.impl;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkflowVO;
import com.baiyi.opscloud.facade.workorder.WorkOrderTicketFacade;
import com.baiyi.opscloud.facade.workorder.WorkOrderTicketNodeFacade;
import com.baiyi.opscloud.facade.workorder.WorkOrderTicketSubscriberFacade;
import com.baiyi.opscloud.packer.workorder.TicketPacker;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.service.workorder.*;
import com.baiyi.opscloud.workorder.approve.ITicketApprove;
import com.baiyi.opscloud.workorder.approve.factory.WorkOrderTicketApproveFactory;
import com.baiyi.opscloud.workorder.constants.ApprovalTypeConstants;
import com.baiyi.opscloud.workorder.constants.NodeTypeConstants;
import com.baiyi.opscloud.workorder.constants.OrderTicketPhaseCodeConstants;
import com.baiyi.opscloud.workorder.exception.TicketException;
import com.baiyi.opscloud.workorder.helper.TicketNoticeHelper;
import com.baiyi.opscloud.workorder.helper.strategy.impl.SendAuditNotice;
import com.baiyi.opscloud.workorder.processor.ITicketProcessor;
import com.baiyi.opscloud.workorder.processor.factory.WorkOrderTicketProcessorFactory;
import com.baiyi.opscloud.workorder.query.factory.WorkOrderTicketEntryQueryFactory;
import com.baiyi.opscloud.workorder.util.WorkflowUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工单票据外观设计
 *
 * @Author baiyi
 * @Date 2022/1/11 2:13 PM
 * @Version 1.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Slf4j
@Component
@RequiredArgsConstructor
public class WorkOrderTicketFacadeImpl implements WorkOrderTicketFacade {

    private final WorkOrderService workOrderService;

    private final TicketPacker ticketPacker;

    private final WorkOrderTicketService ticketService;

    private final WorkOrderTicketEntryService ticketEntryService;

    private final WorkOrderTicketSubscriberService ticketSubscriberService;

    private final UserService userService;

    private final WorkOrderTicketNodeFacade ticketNodeFacade;

    private final WorkOrderTicketNodeService ticketNodeService;

    private final WorkOrderTicketSubscriberFacade ticketSubscriberFacade;

    private final TicketNoticeHelper ticketNoticeHelper;

    private final RedisUtil redisUtil;

    @Override
    public DataTable<WorkOrderTicketVO.Ticket> queryTicketPage(WorkOrderTicketParam.TicketPageQuery pageQuery) {
        DataTable<WorkOrderTicket> table = ticketService.queryPageByParam(pageQuery);
        List<WorkOrderTicketVO.Ticket> data = BeanCopierUtil.copyListProperties(table.getData(), WorkOrderTicketVO.Ticket.class).stream().peek(e ->
                ticketPacker.wrap(e, pageQuery)
        ).collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public DataTable<WorkOrderTicketVO.Ticket> queryMyTicketPage(WorkOrderTicketParam.MyTicketPageQuery pageQuery) {
        pageQuery.setUsername(SessionHolder.getUsername());
        DataTable<WorkOrderTicket> table = ticketService.queryPageByParam(pageQuery);

        List<WorkOrderTicketVO.Ticket> data = BeanCopierUtil.copyListProperties(table.getData(), WorkOrderTicketVO.Ticket.class).stream().peek(e ->
                ticketPacker.wrap(e, pageQuery)
        ).collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public WorkOrderTicketVO.TicketView createTicket(WorkOrderTicketParam.CreateTicket createTicket) {
        final String username = SessionHolder.getUsername();
        WorkOrderTicket workOrderTicket = ticketService.getNewTicketByUser(createTicket.getWorkOrderKey(), username);
        WorkOrder workOrder = workOrderService.getByKey(createTicket.getWorkOrderKey());
        if (workOrderTicket == null) {
            workOrderTicket = createNewTicket(workOrder, username);
        }
        return toTicketView(workOrderTicket);
    }

    @Override
    public WorkOrderTicketVO.TicketView saveTicket(WorkOrderTicketParam.SubmitTicket saveTicket) {
        WorkOrderTicket workOrderTicket = ticketService.getById(saveTicket.getTicketId());
        preSaveHandle(workOrderTicket, saveTicket);
        return toTicketView(workOrderTicket);
    }

    @Override
    public WorkOrderTicketVO.TicketView submitTicket(WorkOrderTicketParam.SubmitTicket submitTicket) {
        WorkOrderTicket ticket = ticketService.getById(submitTicket.getTicketId());
        preSaveHandle(ticket, submitTicket);
        // 验证工单完整性
        verifyTicket(ticket);
        // 提交工单 变更工单进度
        ticket.setTicketPhase(OrderTicketPhaseCodeConstants.TOAUDIT.name());
        // 设置工单开始时间
        ticket.setStartTime(new Date());
        ticketService.update(ticket);
        // 发布订阅人
        ticketSubscriberFacade.publish(ticket);
        // 工单通知
        ticketNoticeHelper.send(ticket);
        return toTicketView(ticket);
    }

    @Override
    public WorkOrderTicketVO.TicketView approveTicket(WorkOrderTicketParam.ApproveTicket approveTicket) {
        if (StringUtils.isBlank(approveTicket.getApprovalComment())) {
            // 设置默认的审批意见
            approveTicket.setApprovalComment(ApprovalTypeConstants.getDesc(approveTicket.getApprovalType()));
        }
        ITicketApprove iTicketApprove = WorkOrderTicketApproveFactory.getByApprovalType(approveTicket.getApprovalType());
        if (iTicketApprove == null) {
            throw new TicketException("审批类型不正确!");
        }
        iTicketApprove.approve(approveTicket);
        WorkOrderTicket ticket = ticketService.getById(approveTicket.getTicketId());
        // 工单通知
        ticketNoticeHelper.send(ticket);
        return toTicketView(ticket);
    }

    @Override
    public HttpResult approveTicket(WorkOrderTicketParam.OutApproveTicket outApproveTicket) {
        // 鉴权
        String key = SendAuditNotice.buildKey(outApproveTicket.getTicketId(), outApproveTicket.getUsername());
        if (!redisUtil.hasKey(key)) {
            return new HttpResult(ErrorEnum.WORKORDER_INVALID_TOKEN);
        }
        String token = (String) redisUtil.get(key);
        redisUtil.del(key);
        if (!token.equals(outApproveTicket.getToken())) {
            return new HttpResult(ErrorEnum.WORKORDER_INVALID_TOKEN);
        }
        SessionHolder.setUsername(outApproveTicket.getUsername());
        WorkOrderTicketParam.ApproveTicket approveTicket = WorkOrderTicketParam.ApproveTicket.builder()
                .ticketId(outApproveTicket.getTicketId())
                .approvalType(outApproveTicket.getApprovalType())
                .approvalComment(ApprovalTypeConstants.getDesc(outApproveTicket.getApprovalType()))
                .build();
        try {
            this.approveTicket(approveTicket);
        } catch (Exception e) {
            return HttpResult.builder()
                    .success(false)
                    .msg("Approval failed")
                    .build();
        }
        return HttpResult.builder()
                .success(true)
                .msg("Approval completed")
                .build();
    }

    @Override
    public WorkOrderTicketVO.TicketView getTicketEntries(int ticketId, String workOrderKey) {
        return ticketPacker.toTicketEntries(ticketId, workOrderKey);
    }

    /**
     * 验证工单完整性
     *
     * @param workOrderTicket
     */
    private void verifyTicket(WorkOrderTicket workOrderTicket) {
        if (ticketEntryService.countByWorkOrderTicketId(workOrderTicket.getId()) == 0) {
            throw new TicketException("工单选项(条目)未配置");
        }
        WorkOrder workOrder = workOrderService.getById(workOrderTicket.getWorkOrderId());
        // 验证工作流节审批配置
        ticketNodeFacade.verifyWorkflowNodes(workOrder, workOrderTicket);
    }

    private void preSaveHandle(WorkOrderTicket workOrderTicket, WorkOrderTicketParam.SubmitTicket saveTicket) {
        if (workOrderTicket == null) {
            throw new TicketException("工单不存在！");
        }
        final String username = SessionHolder.getUsername();
        if (!workOrderTicket.getUsername().equals(username)) {
            throw new TicketException("只有本人才能保存工单！");
        }
        if (!OrderTicketPhaseCodeConstants.NEW.name().equals(workOrderTicket.getTicketPhase())) {
            throw new TicketException("工单状态不允许变更！");
        }
        saveTicketComment(workOrderTicket, saveTicket);
        WorkOrder workOrder = workOrderService.getById(workOrderTicket.getWorkOrderId());
        Map<String, WorkflowVO.Node> originalWorkflowNodeMap = WorkflowUtil.toNodeMap(workOrder.getWorkflow());
        saveTicket.getWorkflowView().getNodes().forEach(node -> {
            if (NodeTypeConstants.USER_LIST.getCode() == node.getType() && node.getAuditUser() != null) {
                // 更新用户指定审批人
                ticketNodeFacade.updateWorkflowNodeAuditUser(saveTicket.getTicketId(), node.getName(), node.getAuditUser());
            }
        });
    }

    private void saveTicketComment(WorkOrderTicket workOrderTicket, WorkOrderTicketParam.SubmitTicket saveTicket) {
        if (StringUtils.isEmpty(saveTicket.getComment())) {
            if (StringUtils.isEmpty(workOrderTicket.getComment())) {
                return;
            }
        } else {
            if (saveTicket.getComment().equals(workOrderTicket.getComment())) {
                return;
            }
        }
        workOrderTicket.setComment(saveTicket.getComment());
        ticketService.update(workOrderTicket);
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
                .ticketPhase(OrderTicketPhaseCodeConstants.NEW.name())
                .ticketStatus(0)
                .build();
        ticketService.add(workOrderTicket);
        ticketNodeFacade.createWorkflowNodes(workOrder, workOrderTicket);
        // 增加创建订阅人
        ticketSubscriberFacade.publish(workOrderTicket, user);
        // 更新节点ID
        WorkOrderTicketNode workOrderTicketNode = ticketNodeService.getByUniqueKey(workOrderTicket.getId(), 0);
        workOrderTicket.setNodeId(workOrderTicketNode.getId());
        ticketService.update(workOrderTicket);
        return workOrderTicket;
    }

    @Override
    public WorkOrderTicketVO.TicketView getTicket(Integer ticketId) {
        WorkOrderTicket workOrderTicket = ticketService.getById(ticketId);
        return toTicketView(workOrderTicket);
    }

    @Override
    public List<WorkOrderTicketVO.Entry> queryTicketEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        WorkOrderTicket ticket = ticketService.getById(entryQuery.getWorkOrderTicketId());
        if (ticket == null) {
            throw new TicketException("工单票据不存在！");
        }
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        return WorkOrderTicketEntryQueryFactory.getByKey(workOrder.getWorkOrderKey()).query(entryQuery);
    }

    @Override
    public WorkOrderTicketVO.TicketView updateTicketEntry(WorkOrderTicketEntryParam.TicketEntry ticketEntry) {
        WorkOrderTicket workOrderTicket = ticketService.getById(ticketEntry.getWorkOrderTicketId());
        WorkOrder workOrder = workOrderService.getById(workOrderTicket.getWorkOrderId());
        ITicketProcessor iTicketProcessor = WorkOrderTicketProcessorFactory.getByKey(workOrder.getWorkOrderKey());
        if (iTicketProcessor == null) {
            throw new TicketException("工单类型不正确！");
        }
        iTicketProcessor.update(ticketEntry);
        return toTicketView(workOrderTicket);
    }

    @Override
    public void addTicketEntry(WorkOrderTicketEntryParam.TicketEntry ticketEntry) {
        if (IdUtil.isEmpty(ticketEntry.getWorkOrderTicketId())) {
            throw new TicketException("新增工单条目错误: 对象为空！");
        }
        WorkOrderTicket workOrderTicket = ticketService.getById(ticketEntry.getWorkOrderTicketId());
        if (!SessionHolder.equalsUsername(workOrderTicket.getUsername())) {
            throw new TicketException("不合法的请求: 只有工单创建人才能新增条目！");
        }
        WorkOrder workOrder = workOrderService.getById(workOrderTicket.getWorkOrderId());
        ITicketProcessor iTicketProcessor = WorkOrderTicketProcessorFactory.getByKey(workOrder.getWorkOrderKey());
        if (iTicketProcessor == null) {
            throw new TicketException("工单类型不正确！");
        }
        // 验证
        iTicketProcessor.verify(ticketEntry);
        // 新增
        ticketEntryService.add(ticketEntry);
    }

    @Override
    public void deleteTicketEntry(Integer ticketEntryId) {
        WorkOrderTicketEntry ticketEntry = ticketEntryService.getById(ticketEntryId);
        if (ticketEntry == null) {
            throw new TicketException("工单条目不存在！");
        }
        WorkOrderTicket workOrderTicket = ticketService.getById(ticketEntry.getWorkOrderTicketId());
        if (!OrderTicketPhaseCodeConstants.NEW.name().equals(workOrderTicket.getTicketPhase())) {
            throw new TicketException("只有新建工单才能修改或删除条目！");
        }
        if (!SessionHolder.equalsUsername(workOrderTicket.getUsername())) {
            throw new TicketException("不合法的请求: 只有工单创建人才能新增条目！");
        }
        ticketEntryService.deleteById(ticketEntryId);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteTicketById(Integer ticketId) {
        WorkOrderTicket ticket = ticketService.getById(ticketId);
        if (ticket == null) {
            return;
        }
        // 删除所有条目
        List<WorkOrderTicketEntry> entries = ticketEntryService.queryByWorkOrderTicketId(ticketId);
        if (!CollectionUtils.isEmpty(entries)) {
            for (WorkOrderTicketEntry entry : entries) {
                ticketEntryService.deleteById(entry.getId());
            }
        }
        // 删除节点
        List<WorkOrderTicketNode> nodes = ticketNodeService.queryByWorkOrderTicketId(ticketId);
        if (!CollectionUtils.isEmpty(nodes)) {
            for (WorkOrderTicketNode node : nodes) {
                ticketNodeService.deleteById(node.getId());
            }
        }
        // 删除订阅人
        List<WorkOrderTicketSubscriber> subscribers = ticketSubscriberService.queryByWorkOrderTicketId(ticketId);
        if (!CollectionUtils.isEmpty(subscribers)) {
            for (WorkOrderTicketSubscriber subscriber : subscribers) {
                ticketSubscriberService.deleteById(subscriber.getId());
            }
        }
        // 删除工单票据
        log.info("删除工单: ticketId={}", ticketId);
        ticketService.deleteById(ticketId);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteTicketByWorkOrderAndPhase(int workOrderId, String phase) {
        if (StringUtils.isNotBlank(phase) &&
                OrderTicketPhaseCodeConstants.NEW.getPhase().equalsIgnoreCase(phase) ||
                OrderTicketPhaseCodeConstants.TOAUDIT.getPhase().equalsIgnoreCase(phase)) {
            List<WorkOrderTicket> tickets = ticketService.queryByParam(workOrderId, phase);
            if (!CollectionUtils.isEmpty(tickets)) {
                for (WorkOrderTicket ticket : tickets) {
                    this.deleteTicketById(ticket.getId());
                }
            }
        } else {
            throw new TicketException("当前阶段工单不允许删除: {}", phase);
        }
    }

    private WorkOrderTicketVO.TicketView toTicketView(WorkOrderTicket workOrderTicket) {
        return ticketPacker.toTicketView(workOrderTicket);
    }

}
