package com.baiyi.opscloud.domain.vo.application;

import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/13 10:32 上午
 * @Version 1.0
 */
public class ApplicationResourceVO {

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class Resource extends BaseVO implements DsInstanceVO.IDsInstance {

        private DsInstanceVO.Instance instance;

        private DsAssetVO.Asset asset;

        private List<AssetContainer> assetContainers;

        private String instanceUuid;

        @ApiModelProperty(value = "主键", example = "1")
        private Integer id;

        @NotNull(message = "应用id不能为空")
        private Integer applicationId;

        private String name;

        @ApiModelProperty(value = "虚拟资源", example = "true")
        @Builder.Default
        private Boolean virtualResource = false;

        @NotNull(message = "资源类型不能为空")
        private String resourceType;

        @NotNull(message = "业务id不能为空")
        private Integer businessId;

        @NotNull(message = "业务类型不能为空")
        private Integer businessType;

        private String comment;

        /**
         * 前端选择用
         */
        @Builder.Default
        private Boolean checked = false;
        
    }
}
