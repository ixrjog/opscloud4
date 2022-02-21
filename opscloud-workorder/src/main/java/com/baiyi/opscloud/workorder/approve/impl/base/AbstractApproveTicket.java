package com.baiyi.opscloud.workorder.approve.impl.base;

import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketNode;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketParam;
import com.baiyi.opscloud.domain.vo.workorder.SimpleApprover;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketNodeService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketService;
import com.baiyi.opscloud.workorder.approve.ITicketApprove;
import com.baiyi.opscloud.workorder.approve.factory.WorkOrderTicketApproveFactory;
import com.baiyi.opscloud.workorder.constants.ApprovalTypeConstants;
import com.baiyi.opscloud.workorder.exception.TicketCommonException;
import com.baiyi.opscloud.workorder.helper.TicketApproverHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;
import java.util.Date;

import static com.baiyi.opscloud.common.config.ThreadPoolTaskConfiguration.TaskPools.CORE;

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
    private TicketApproverHelper ticketApproverHelper;

    @Override
    @Async(value = CORE)
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
        ticketApproverHelper.wrap(simpleApprover, ticket);
        if (!simpleApprover.getIsApprover()) {
            throw new TicketCommonException("禁止操作: 非当前审批人!");
        }
        if (StringUtils.isEmpty(ticketNode.getUsername())) {
            ticketNode.setUsername(SessionUtil.getUsername()); // 审批人
        }
        ticketNode.setApprovalStatus(approveTicket.getApprovalType()); // 审批状态
        ticketNode.setComment(approveTicket.getApprovalComment()); // 审批意见
        ticketNodeService.update(ticketNode); // 保存当前审批节点数据
        postHandle(ticket, ticketNode);
    }

    abstract protected void postHandle(WorkOrderTicket ticket, WorkOrderTicketNode ticketNode);

    protected void updateTicket(WorkOrderTicket ticket, boolean isFinished) {
        if (isFinished)
            ticket.setEndTime(new Date());
        ticketService.update(ticket);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        WorkOrderTicketApproveFactory.register(this);
    }

}
