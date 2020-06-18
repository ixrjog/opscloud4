package com.baiyi.opscloud.domain.vo.workorder;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/30 11:30 上午
 * @Version 1.0
 */
public class ApprovalOptionsVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ApprovalOptions {
        private List<ApprovalOption> approvalOptions;
    }

    @Data
    public static class ApprovalOption {
        private String name;
        private Integer sequence;
        private String comment;
    }

}
