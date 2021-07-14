package com.baiyi.opscloud.domain.vo.application;

import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/13 10:32 上午
 * @Version 1.0
 */
public class ApplicationResourceVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Resource extends BaseVO {

        private DsAssetVO.Asset asset;

        private List<AssetContainer> assetContainers;

        @ApiModelProperty(value = "主键", example = "1")
        private Integer id;

        private Integer applicationId;

        private String name;

        private Boolean virtualResource;

        private String resourceType;

        private Integer businessId;

        private Integer businessType;

        private String comment;

    }
}
