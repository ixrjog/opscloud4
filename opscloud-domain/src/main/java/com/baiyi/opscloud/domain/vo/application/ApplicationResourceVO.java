package com.baiyi.opscloud.domain.vo.application;

import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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

        @NotNull(message = "应用id不能为空")
        private Integer applicationId;

        private String name;

        @ApiModelProperty(value = "虚拟资源", example = "true")
        private Boolean virtualResource;

        @NotNull(message = "资源类型不能为空")
        private String resourceType;

        @NotNull(message = "业务id不能为空")
        private Integer businessId;

        @NotNull(message = "业务类型不能为空")
        private Integer businessType;

        private String comment;

    }
}
