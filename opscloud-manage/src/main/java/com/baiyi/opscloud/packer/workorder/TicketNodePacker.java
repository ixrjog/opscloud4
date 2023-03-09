package com.baiyi.opscloud.packer.workorder;

import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketNode;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderNodeVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketNodeService;
import com.baiyi.opscloud.workorder.constants.ApprovalTypeConstants;
import com.baiyi.opscloud.workorder.constants.OrderTicketPhaseCodeConstants;
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
public class TicketNodePacker {

    private final WorkOrderTicketNodeService workOrderTicketNodeService;

    private final UserService userService;

    public void wrap(WorkOrderTicketVO.TicketView ticketView) {
        if (OrderTicketPhaseCodeConstants.NEW.name().equals(ticketView.getTicket().getTicketPhase())) {
            return; // 新建工单不需要展示审批视图
        }
        int parentId = 0;
        List<WorkOrderNodeVO.Stage> stages = Lists.newArrayList();
        int id = 1;
        while (true) {
            WorkOrderTicketNode ticketNode = workOrderTicketNodeService.getByUniqueKey(ticketView.getTicket().getId(), parentId);
            if (ticketNode == null)
                break;
            parentId = ticketNode.getId();
            WorkOrderNodeVO.Stage stage = WorkOrderNodeVO.Stage.builder()
                    .id(id)
                    .name(ticketNode.getNodeName())
                    .state(toState(ticketView.getTicket(), ticketNode))
                    .type("STAGE")
                    .build();
            // 插入审批意见
            if (!StringUtils.isEmpty(ticketNode.getApprovalStatus())) {
                User user = userService.getByUsername(ticketNode.getUsername());
                stage.setPopInfo(
                        WorkOrderNodeVO.PopInfo.builder()
                                .title(user.getDisplayName())
                                .msg(Lists.newArrayList("审批意见:", ticketNode.getComment()))
                                .build()
                );
            }
            stages.add(stage);
            id++;
        }
        ticketView.setNodeView(
                WorkOrderNodeVO.NodeView.builder()
                        .stages(stages)
                        .build()
        );
    }

    /**
     * 'popInfo': {
     * title: '< 通知 >',
     * msg: [ '重要内容', '经党委会研究决定，现任命王铁柱为中共河北省省委书记' ],
     * width: "101",
     * height: "120",
     * }
     *
     * @param workOrderTicket
     * @param workOrderTicketNode
     * @return
     */
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
            return StageConstants.SUCCESS.name();
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
