package com.baiyi.opscloud.domain.param.datasource;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.IRelation;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

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
    @ApiModel
    public static class AssetPageQuery extends SuperPageParam implements IExtend, IRelation {

        @ApiModelProperty(value = "实例id")
        @NotNull(message = "实例id不能为空")
        private Integer instanceId;

        @ApiModelProperty(value = "实例uuid")
        private String instanceUuid;

        @ApiModelProperty(value = "regionId")
        private String regionId;

        @ApiModelProperty(value = "kind")
        private String kind;

        @ApiModelProperty(value = "资产类型")
        @NotNull(message = "资产类型不能为空")
        private String assetType;

        @ApiModelProperty(value = "模糊查询")
        private String queryName;

        @ApiModelProperty(value = "展开")
        private Boolean extend;

        @ApiModelProperty(value = "展示资产关系")
        private Boolean relation;

        @ApiModelProperty(value = "是否有效")
        @Builder.Default
        private Boolean isActive = true;

    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class UserPermissionAssetPageQuery extends SuperPageParam {

        private Integer businessType;

        private Integer userId;

        @ApiModelProperty(value = "实例id")
        @NotNull(message = "实例id不能为空")
        private Integer instanceId;

        @ApiModelProperty(value = "实例uuid")
        private String instanceUuid;

        @ApiModelProperty(value = "资产类型")
        @NotNull(message = "资产类型不能为空")
        private String assetType;

        @ApiModelProperty(value = "模糊查询")
        private String queryName;

    }

    @SuperBuilder(toBuilder = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class BaseAsset {

        @ApiModelProperty(value = "实例id")
        @NotNull
        private Integer instanceId;

        @ApiModelProperty(value = "资产类型")
        @NotNull
        private String assetType;

    }

    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PushAsset extends BaseAsset {
    }

    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PullAsset extends BaseAsset {
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ScanAssetBusiness extends BaseAsset {
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class SetDsInstanceConfig {

        @ApiModelProperty(value = "实例id")
        @NotNull
        private Integer instanceId;

        @ApiModelProperty(value = "实例类型")
        @NotNull
        private String instanceType;

    }
}
