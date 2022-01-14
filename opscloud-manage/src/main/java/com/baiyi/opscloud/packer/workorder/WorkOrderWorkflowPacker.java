package com.baiyi.opscloud.packer.workorder;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.WorkflowUtil;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkflowVO;
import com.baiyi.opscloud.packer.user.UserPacker;
import com.baiyi.opscloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/1/14 1:06 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class WorkOrderWorkflowPacker {

    private final UserService userService;

    private final UserPacker userPacker;

    public void wrap(WorkOrderTicketVO.TicketView ticketView) {
        WorkOrderVO.WorkOrder workOrder = ticketView.getWorkOrder();
        if (workOrder == null) return;
        WorkflowVO.Workflow workflowVO = WorkflowUtil.toWorkflowView(workOrder.getWorkflow());
        List<WorkflowVO.NodeView> nodes = workflowVO.getNodes().stream().map(e -> {
            WorkflowVO.NodeView nodeView = BeanCopierUtil.copyProperties(e, WorkflowVO.NodeView.class);
            try {
                // NodeTypeConstants.SYS.getCode() == nodeView.getType() &&
                if (!CollectionUtils.isEmpty(nodeView.getTags())) {
                    List<User> users = userService.queryByTagKeys(nodeView.getTags());
                    nodeView.setAuditUsers(userPacker.wrapAvatar(users));
                }
            } catch (Exception ex) {
            }
            return nodeView;
        }).collect(Collectors.toList());

        WorkflowVO.WorkflowView workflowView = WorkflowVO.WorkflowView.builder()
                .nodes(nodes)
                .build();
        ticketView.setWorkflowView(workflowView);
    }

}
