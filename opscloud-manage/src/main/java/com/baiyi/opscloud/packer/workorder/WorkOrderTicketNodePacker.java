package com.baiyi.opscloud.packer.workorder;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketNode;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderNodeVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketNodeService;
import com.baiyi.opscloud.workorder.constants.ApprovalTypeConstants;
import com.baiyi.opscloud.workorder.constants.OrderPhaseCodeConstants;
import com.baiyi.opscloud.workorder.constants.StageConstants;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/17 5:58 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class WorkOrderTicketNodePacker {

    private final WorkOrderTicketNodeService workOrderTicketNodeService;

    public void wrap(WorkOrderTicketVO.TicketView ticketView) {
        if (OrderPhaseCodeConstants.NEW.name().equals(ticketView.getWorkOrderTicket().getTicketPhase()))
            return; // 新建工单不需要展示审批视图
        int parentId = 0;
        List<WorkOrderNodeVO.Stage> stages = Lists.newArrayList();
        while (true) {
            WorkOrderTicketNode ticketNode = workOrderTicketNodeService.getByUniqueKey(ticketView.getWorkOrderTicket().getId(), parentId);
            if (ticketNode == null)
                break;
            parentId = ticketNode.getId();
            WorkOrderNodeVO.Stage stage = WorkOrderNodeVO.Stage.builder()
                    .name(ticketNode.getNodeName())
                    .state(toState(ticketView.getWorkOrderTicket(), ticketNode))
                    .type("STAGE")
                    .build();
            stages.add(stage);
        }
        ticketView.setNodeView(
                WorkOrderNodeVO.NodeView.builder()
                        .stages(stages)
                        .build()
        );
    }

    private String toState(WorkOrderTicketVO.Ticket workOrderTicket, WorkOrderTicketNode workOrderTicketNode) {
        String approvalStatus = workOrderTicketNode.getApprovalStatus();
        if (workOrderTicket.getNodeId().equals(workOrderTicketNode.getId())) {
            if (StringUtils.isEmpty(approvalStatus)) {
                return StageConstants.RUNNING.name();
            }
        } else {
            if (StringUtils.isEmpty(approvalStatus)) {
                return StageConstants.not_built.name();
            }
        }
        if (ApprovalTypeConstants.AGREE.name().equals(approvalStatus)) {
            return StageConstants.FINISHED.name();
        }
        if (ApprovalTypeConstants.REJECT.name().equals(approvalStatus)) {
            return StageConstants.FAILURE.name();
        }
        if (ApprovalTypeConstants.CANCEL.name().equals(approvalStatus)) {
            return StageConstants.PAUSED.name();
        }
        return StageConstants.not_built.name();
    }


}
