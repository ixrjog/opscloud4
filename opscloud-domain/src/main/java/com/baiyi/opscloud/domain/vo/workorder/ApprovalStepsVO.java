package com.baiyi.opscloud.domain.vo.workorder;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/30 4:29 下午
 * @Version 1.0
 */
public class ApprovalStepsVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ApprovalDetail {

        private Integer active;
        private List<ApprovalStepsVO.ApprovalStep> approvalSteps;
    }

    @Data
    @Builder
    public static class ApprovalStep {
        private String title;
        private String description;
    }

}
