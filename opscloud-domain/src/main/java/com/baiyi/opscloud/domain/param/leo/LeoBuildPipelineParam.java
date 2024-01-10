package com.baiyi.opscloud.domain.param.leo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2022/11/28 16:45
 * @Version 1.0
 */
public class LeoBuildPipelineParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class GetPipelineRunNodeSteps {

        @Min(value = 0, message = "构建ID不能为空")
        @Schema(description = "构建ID")
        private Integer buildId;

        @NotBlank(message = "节点ID不能为空")
        @Schema(description = "节点ID")
        private String nodeId;

    }

}