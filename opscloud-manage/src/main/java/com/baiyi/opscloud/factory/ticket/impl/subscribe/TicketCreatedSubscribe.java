package com.baiyi.opscloud.factory.ticket.impl.subscribe;

import com.baiyi.opscloud.common.base.TicketPhase;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketSubscribe;
import com.baiyi.opscloud.domain.vo.workorder.ApprovalStepsVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderTicketVO;
import com.baiyi.opscloud.factory.ticket.ITicketSubscribe;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/7 9:24 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class TicketCreatedSubscribe extends BaseTicketSubscribe implements ITicketSubscribe {

    @Override
    public String getKey() {
        return TicketPhase.CREATED.getPhase();
    }

    @Override
    public BusinessWrapper<Boolean> subscribe(OcWorkorderTicket ocWorkorderTicket) {
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public void invokeFlowStep(WorkorderTicketVO.Ticket ticket, String ticketPhase) {
        ApprovalStepsVO.ApprovalStep approvalStep = ApprovalStepsVO.ApprovalStep.builder()
                .title("填写")
                .description("填写完成后提交申请")
                .build();
        ticket.getApprovalDetail().getApprovalSteps().add(approvalStep);
        if(TicketPhase.CREATED.getPhase().equals(ticketPhase))
            ticket.getApprovalDetail().setActive(ACTIVE);
    }

    @Override
    public List<OcWorkorderTicketSubscribe> queryTicketSubscribes(WorkorderTicketVO.Ticket ticket){
        return Lists.newArrayList();
    }
}
