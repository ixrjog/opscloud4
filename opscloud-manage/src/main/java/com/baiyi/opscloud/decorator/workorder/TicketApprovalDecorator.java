package com.baiyi.opscloud.decorator.workorder;

import com.baiyi.opscloud.common.base.TicketPhase;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorder;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketFlow;
import com.baiyi.opscloud.domain.vo.workorder.ApprovalStepsVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderTicketVO;
import com.baiyi.opscloud.factory.ticket.WorkorderTicketSubscribeFactory;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketFlowService;
import com.baiyi.opscloud.service.workorder.OcWorkorderService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/30 5:57 下午
 * @Version 1.0
 */
@Component
public class TicketApprovalDecorator {

    @Resource
    private OcWorkorderService ocWorkorderService;

    @Resource
    private OcWorkorderTicketFlowService ocWorkorderTicketFlowService;

    // 组装 TicketApproval
    public void decorator(WorkorderTicketVO.Ticket ticket) {
        ApprovalStepsVO.ApprovalDetail approvalDetail = new ApprovalStepsVO.ApprovalDetail();
        List<ApprovalStepsVO.ApprovalStep> approvalStepList = Lists.newArrayList();

        approvalDetail.setApprovalSteps(approvalStepList);
        ticket.setApprovalDetail(approvalDetail);

        OcWorkorder ocWorkorder = ocWorkorderService.queryOcWorkorderById(ticket.getWorkorderId());

        String ticketPhase = TicketPhase.CREATED.getPhase();

        if(ticket.getFlowId() != null){
            OcWorkorderTicketFlow ocWorkorderTicketFlow = ocWorkorderTicketFlowService.queryOcWorkorderTicketFlowById(ticket.getFlowId());
            if (ocWorkorderTicketFlow != null)
                ticketPhase = ocWorkorderTicketFlow.getFlowName();
        }

        // 填写阶段
        WorkorderTicketSubscribeFactory.getTicketSubscribeByKey(TicketPhase.CREATED.getPhase()).invokeFlowStep(ticket, ticketPhase);

        WorkorderTicketSubscribeFactory.getTicketSubscribeByKey(TicketPhase.APPLIED.getPhase()).invokeFlowStep(ticket, ticketPhase);

        // 要求组织架构上级审批
        if (ocWorkorder.getOrgApproval())
            WorkorderTicketSubscribeFactory.getTicketSubscribeByKey(TicketPhase.ORG_APPROVAL.getPhase()).invokeFlowStep(ticket, ticketPhase);

        // 审批组配置
        if (ocWorkorder.getApprovalGroupId() != 0)
            WorkorderTicketSubscribeFactory.getTicketSubscribeByKey(TicketPhase.USERGROUP_APPROVAL.getPhase()).invokeFlowStep(ticket, ticketPhase);

        // 工单结束阶段
        WorkorderTicketSubscribeFactory.getTicketSubscribeByKey(TicketPhase.FINALIZED.getPhase()).invokeFlowStep(ticket, ticketPhase);
    }


}
