package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorder;
import com.baiyi.opscloud.domain.vo.org.OrgApprovalVO;
import com.baiyi.opscloud.domain.vo.workorder.ApprovalStepsVO;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderTicketVO;
import com.baiyi.opscloud.service.workorder.OcWorkorderService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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
    private DepartmentMemberDecorator departmentMemberDecorator;

    // 组装 TicketApproval
    public void decorator(OcWorkorderTicketVO.Ticket ticket) {
        ApprovalStepsVO.ApprovalDetail approvalDetail = new ApprovalStepsVO.ApprovalDetail();

        List<ApprovalStepsVO.ApprovalStep> approvalStepList = Lists.newArrayList();

        OcWorkorder ocWorkorder = ocWorkorderService.queryOcWorkorderById(ticket.getWorkorderId());
        ApprovalStepsVO.ApprovalStep approvalStepWrite = ApprovalStepsVO.ApprovalStep.builder()
                .title("填写")
                .description("填写完成后提交申请")
                .build();
        approvalStepList.add(approvalStepWrite);

        // 要求组织架构上级审批
        if (ocWorkorder.getOrgApproval()) {
            // 组织架构审批
            OrgApprovalVO.OrgApproval orgApproval = departmentMemberDecorator.decorator(ticket.getUserId());
            ApprovalStepsVO.ApprovalStep orgApprovalStep = ApprovalStepsVO.ApprovalStep.builder()
                    .title("上级审批")
                    .description(getDescription(orgApproval))
                    .build();
            approvalStepList.add(orgApprovalStep);
        }

        //  FINALIZED
        ApprovalStepsVO.ApprovalStep approvalStepFinalized = ApprovalStepsVO.ApprovalStep.builder()
                .title("完成")
                .description("工单结束")
                .build();
        approvalStepList.add(approvalStepFinalized);

        approvalDetail.setActive(1);

        approvalDetail.setApprovalSteps(approvalStepList);
        ticket.setApprovalDetail(approvalDetail);
    }

    private String getDescription(OrgApprovalVO.OrgApproval orgApproval) {
        if (orgApproval.getIsError())
            return orgApproval.getErrorMsg();
        if (orgApproval.getIsApprovalAuthority())
            return "本人拥有审批权";
        if (orgApproval.getPreferenceDeptMember() != null)
            return orgApproval.getPreferenceDeptMember().getDisplayName();
        return Joiner.on(",").join(orgApproval.getAlternativeDeptMembers().stream().map(e -> e.getDisplayName()).collect(Collectors.toList()));
    }
}
