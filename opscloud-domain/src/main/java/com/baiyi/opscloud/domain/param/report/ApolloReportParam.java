package com.baiyi.opscloud.domain.param.report;

import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2023/7/17 16:06
 * @Version 1.0
 */
public class ApolloReportParam {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ApolloReleaseReport {

        @Schema(description = "应用名称")
        private String applicationName;

        private final String assetType = DsAssetTypeConstants.APOLLO_INTERCEPT_RELEASE.name();

        @Schema(description = "数据源实例ID")
        @NotNull(message = "必须指定数据源实例ID")
        private Integer instanceId;

        private String envName;

        private String instanceUuid;

        private String gray;

    }

}