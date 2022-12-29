package com.baiyi.opscloud.domain.param.leo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

/**
 * @Author baiyi
 * @Date 2022/12/5 17:31
 * @Version 1.0
 */
public class LeoDeployParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DoDeploy {

        @Min(value = 0, message = "关联任务ID不能为空")
        @ApiModelProperty(value = "关联任务ID")
        private Integer jobId;

        @ApiModelProperty(value = "构建ID")
        private Integer buildId;

        @Min(value = 0, message = "Deployment资产ID不能为空")
        @ApiModelProperty(value = "Deployment资产ID")
        private Integer assetId;

        @ApiModelProperty(value = "部署类型")
        private String deployType;

    }

}
