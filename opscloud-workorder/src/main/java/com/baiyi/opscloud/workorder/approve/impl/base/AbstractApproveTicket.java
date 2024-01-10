package com.baiyi.opscloud.workorder.approve.impl.base;

import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketNode;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketParam;
import com.baiyi.opscloud.domain.vo.workorder.SimpleApprover;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketNodeService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketService;
import com.baiyi.opscloud.workorder.approve.ITicketApprove;
import com.baiyi.opscloud.workorder.approve.factory.WorkOrderTicketApproveFactory;
import com.baiyi.opscloud.workorder.constants.ApprovalTypeConstants;
import com.baiyi.opscloud.workorder.exception.TicketException;
import com.baiyi.opscloud.workorder.helper.TicketApproveHelper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2022/1/19 3:04 PM
 * @Version 1.0
 */
public abstract class AbstractApproveTicket implements ITicketApprove, InitializingBean {

    @Resource
    private WorkOrderTicketService ticketService;

    @Resource
    protected WorkOrderTicketNodeService ticketNodeService;

    @Resource
    private TicketApproveHelper ticketApproveHelper;

    @Override
    @Async
    public void approve(WorkOrderTicketParam.ApproveTicket approveTicket) {
        if (StringUtils.isBlank(approveTicket.getApprovalComment())) {
            // 设置默认的审批意见
            approveTicket.setApprovalComment(ApprovalTypeConstants.getDesc(approveTicket.getApprovalType()));
        }
        WorkOrderTicket ticket = ticketService.getById(approveTicket.getTicketId());
        WorkOrderTicketNode ticketNode = ticketNodeService.getById(ticket.getNodeId());
        SimpleApprover simpleApprover = SimpleApprover.builder()
                .ticketId(ticket.getId())
                .build();
        ticketApproveHelper.wrap(simpleApprover, ticket);
        if (!simpleApprover.getIsApprover()) {
            throw new TicketException("非当前审批人禁止操作!");
        }
        if (StringUtils.isEmpty(ticketNode.getUsername())) {
            // 审批人
            ticketNode.setUsername(SessionHolder.getUsername());
        }
        // 审批状态
        ticketNode.setApprovalStatus(approveTicket.getApprovalType());
        // 审批意见
        ticketNode.setComment(approveTicket.getApprovalComment());
        // 保存当前审批节点数据
        ticketNodeService.update(ticketNode);
        postHandle(ticket, ticketNode);
    }

    /**
     * 后处理
     * @param ticket
     * @param ticketNode
     */
    abstract protected void postHandle(WorkOrderTicket ticket, WorkOrderTicketNode ticketNode);

    protected void updateTicket(WorkOrderTicket ticket, boolean isFinished) {
        if (isFinished) {
            ticket.setEndTime(new Date());
        }
        ticketService.update(ticket);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        WorkOrderTicketApproveFactory.register(this);
    }

}