package com.baiyi.opscloud.domain.param.datasource;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.IRelation;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2021/6/18 5:27 下午
 * @Version 1.0
 */
public class DsAssetParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AssetPageQuery extends SuperPageParam implements IExtend, IRelation {

        @Schema(description = "实例ID")
        @NotNull(message = "实例ID不能为空")
        private Integer instanceId;

        @Schema(description = "实例UUID")
        private String instanceUuid;

        @Schema(description = "regionId")
        private String regionId;

        @Schema(description = "assetKey")
        private String assetKey;

        @Schema(description = "kind")
        private String kind;

        @Schema(description = "资产类型")
        @NotNull(message = "资产类型不能为空")
        private String assetType;

        @Schema(description = "模糊查询")
        private String queryName;

        @Schema(description = "展开")
        private Boolean extend;

        @Schema(description = "展示资产关系")
        private Boolean relation;

        @Schema(description = "是否有效")
        @Builder.Default
        private Boolean isActive = true;

    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class UserPermissionAssetPageQuery extends SuperPageParam {

        private Integer businessType;

        private Integer userId;

        @Schema(description = "实例ID")
        @NotNull(message = "实例id不能为空")
        private Integer instanceId;

        @Schema(description = "实例UUID")
        private String instanceUuid;

        @Schema(description = "资产类型")
        @NotNull(message = "资产类型不能为空")
        private String assetType;

        @Schema(description = "模糊查询")
        private String queryName;

    }

    @SuperBuilder(toBuilder = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class BaseAsset {

        @Schema(description = "实例ID")
        @NotNull
        private Integer instanceId;

        @Schema(description = "资产类型")
        @NotNull
        private String assetType;

    }

    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class PushAsset extends BaseAsset {
    }

    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class PullAsset extends BaseAsset {
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class ScanAssetBusiness extends BaseAsset {
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class SetDsInstanceConfig {

        @Schema(description = "实例ID")
        @NotNull
        private Integer instanceId;

        @Schema(description = "实例类型")
        @NotNull
        private String instanceType;

    }

}