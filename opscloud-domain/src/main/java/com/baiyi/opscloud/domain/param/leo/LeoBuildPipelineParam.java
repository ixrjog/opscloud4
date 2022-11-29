package com.baiyi.opscloud.domain.param.leo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

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
    public static class GetPipelineRunNodeSteps {

        @Min(value = 0, message = "构建ID不能为空")
        @ApiModelProperty(value = "构建ID")
        private Integer buildId;

        @NotBlank(message = "节点ID不能为空")
        @ApiModelProperty(value = "节点ID")
        private String nodeId;

    }

}
