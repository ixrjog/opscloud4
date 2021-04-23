package com.baiyi.opscloud.domain.vo.workorder;

import com.baiyi.opscloud.domain.vo.user.UserVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/28 6:42 下午
 * @Version 1.0
 */
public class WorkorderTicketVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Ticket {

        private List<WorkorderTicketEntryVO.Entry> ticketEntries; // 工单条目
        private ApprovalStepsVO.ApprovalDetail approvalDetail; // 审批步骤
        private WorkorderVO.Workorder workorder; // 工单
        private String ago; // 工单申请时间
        private UserVO.User user; // 发起人
        private Boolean isInApproval; // 审批中
        private Boolean isAllowApproval; // 允许审批
        private Boolean isAllowDelete; // 允许删除

        private Integer id;
        private Integer workorderId;
        private Integer userId;
        private String username;
        private Integer flowId;
        private String comment;
        private String ticketPhase;
        private Integer ticketStatus;
        private Date startTime;
        private Date endTime;
        private Date createTime;
        private Date updateTime;
        private String userDetail;

        private Boolean executorResult; // 执行结果
        private Boolean approvalStatus; // 审批结果
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TicketApproval {
        private Integer stepActive;
        private List<ApprovalStep> approvalSteps;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ApprovalStep {
        private String title;
        private String description;
    }
}
