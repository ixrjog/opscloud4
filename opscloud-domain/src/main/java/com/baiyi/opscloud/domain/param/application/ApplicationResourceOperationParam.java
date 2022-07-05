package com.baiyi.opscloud.domain.param.application;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2022/7/5 10:12
 * @Version 1.0
 */
public class ApplicationResourceOperationParam {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class OperationResource {

        @ApiModelProperty(value = "应用资源ID")
        private Integer resourceId;

        @ApiModelProperty(value = "操作类型")
        @NotNull(message = "必须指定操作类型")
        private String operationType;

        @ApiModelProperty(value = "操作描述")
        private String comment;

    }

}
