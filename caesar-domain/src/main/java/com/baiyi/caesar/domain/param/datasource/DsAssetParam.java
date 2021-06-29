package com.baiyi.caesar.domain.param.datasource;

import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.domain.param.IRelation;
import com.baiyi.caesar.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2021/6/18 5:27 下午
 * @Version 1.0
 */
public class DsAssetParam {

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class AssetPageQuery extends PageParam implements IExtend, IRelation {

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

        @ApiModelProperty(value = "展开")
        private Boolean extend;

        @ApiModelProperty(value = "展示资产关系")
        private Boolean relation;

        @ApiModelProperty(value = "是否有效")
        @Builder.Default
        private Boolean isActive = true;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class  PullAsset {

        @ApiModelProperty(value = "实例id")
        @NotNull
        private Integer instanceId;

        @ApiModelProperty(value = "资产类型")
        @NotNull
        private String assetType;

    }

}
