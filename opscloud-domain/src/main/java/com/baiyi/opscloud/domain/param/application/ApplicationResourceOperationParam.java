package com.baiyi.opscloud.domain.param.application;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2022/7/5 10:12
 * @Version 1.0
 */
public class ApplicationResourceOperationParam {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class OperationResource {

        @Schema(description = "应用资源ID")
        private Integer resourceId;

        @Schema(description = "操作类型")
        @NotNull(message = "必须指定操作类型")
        private String operationType;

        @Schema(description = "操作描述")
        private String comment;

    }

}