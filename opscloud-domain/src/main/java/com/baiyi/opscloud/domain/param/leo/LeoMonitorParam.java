package com.baiyi.opscloud.domain.param.leo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2023/7/20 09:50
 * @Version 1.0
 */
public class LeoMonitorParam {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QueryLatestBuild {

        @NotNull(message = "必须指定查询条目数")
        @Max(value = 20)
        private Integer size;

        private Boolean isFinish;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QueryLatestDeploy {

        @NotNull(message = "必须指定查询条目数")
        @Max(value = 20)
        private Integer size;

        private Boolean isFinish;

    }

}