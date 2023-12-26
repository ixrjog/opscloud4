package com.baiyi.opscloud.domain.param.datasource;

import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2023/7/17 10:47
 * @Version 1.0
 */
public class DsCustomAssetParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ApolloReleaseAssetPageQuery extends SuperPageParam implements IExtend {

        @Schema(description = "资产类型")
        private final String assetType = DsAssetTypeConstants.APOLLO_INTERCEPT_RELEASE.name();

        @Schema(description = "实例ID")
        @NotNull(message = "实例ID不能为空")
        private Integer instanceId;

        @Schema(description = "实例UUID")
        private String instanceUuid;

        @Schema(description = "环境名称")
        private String envName;

        @Schema(description = "应用名称")
        private String applicationName;

        @Schema(description = "查询灰度发布")
        private Boolean isGray;

        @Schema(description = "展开")
        private Boolean extend;

    }

}