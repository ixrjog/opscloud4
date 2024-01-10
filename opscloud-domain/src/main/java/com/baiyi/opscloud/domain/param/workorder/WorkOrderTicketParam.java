package com.baiyi.opscloud.domain.param.workorder;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkflowVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2022/1/10 2:42 PM
 * @Version 1.0
 */
public class WorkOrderTicketParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Schema
    public static class MyTicketPageQuery extends TicketPageQuery {
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class TicketPageQuery extends PageParam implements IExtend {

        @Schema(description = "用户名")
        private String username;

        @Schema(description = "工单ID")
        private Integer workOrderId;

        @Schema(description = "工单票据阶段")
        private String ticketPhase;

        @Schema(description = "工单票据状态")
        private Integer ticketStatus;

        @Schema(description = "展开")
        private Boolean extend;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class CreateTicket {
        @NotBlank(message = "工单Key不能为空！")
        private String workOrderKey;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ApproveTicket {
        @NotNull(message = "必须指定工单票据ID")
        @Schema(description = "工单票据ID")
        private Integer ticketId;

        /**
         * 审批动作，取值和说明如下：
         * <p>
         * AGREE：同意
         * CANCEL：取消
         * REJECT：拒绝
         */
        @Schema(description = "审批动作")
        private String approvalType;

        @Schema(description = "审批说明")
        private String approvalComment;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class OutApproveTicket {

        @NotNull(message = "必须指定工单票据ID")
        @Schema(description = "工单票据ID")
        private Integer ticketId;

        @Schema(description = "用户名")
        private String username;

        /**
         * 审批动作，取值和说明如下：
         * <p>
         * AGREE：同意
         * CANCEL：取消
         * REJECT：拒绝
         */
        @Schema(description = "审批动作")
        private String approvalType;

        @Schema(description = "移动端审批令牌")
        private String token;

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Schema
    public static class SubmitTicket {

        @NotNull(message = "必须指定工单票据ID")
        @Schema(description = "工单票据ID")
        private Integer ticketId;

        @Schema(description = "工单说明")
        private String comment;

        @Schema(description = "工作流")
        private WorkflowVO.WorkflowView workflowView;

    }

}