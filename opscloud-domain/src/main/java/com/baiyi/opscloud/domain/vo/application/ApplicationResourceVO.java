package com.baiyi.opscloud.domain.vo.application;

import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Date 2021/7/13 10:32 上午
 * @version 1.0.0
 * @author liangjian
 */
public class ApplicationResourceVO {

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Resource extends BaseVO implements DsInstanceVO.IDsInstance, TagVO.ITags {

        private List<TagVO.Tag> tags;

        private DsInstanceVO.Instance instance;

        private DsAssetVO.Asset asset;

        private List<AssetContainer> assetContainers;

        private String instanceUuid;

        @Schema(description = "主键", example = "1")
        private Integer id;

        private Integer applicationId;

        private String name;

        @Schema(description = "虚拟资源", example = "true")
        @Builder.Default
        private Boolean virtualResource = false;

        private String resourceType;

        private Integer businessId;

        private Integer businessType;

        private String comment;

        /**
         * 前端选择用
         */
        @Builder.Default
        private Boolean checked = false;

    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class BaseResource extends BaseVO implements Serializable {

        @Serial
        private static final long serialVersionUID = -782607267036174626L;

        @Schema(description = "主键", example = "1")
        private Integer id;

        @NotNull(message = "应用id不能为空")
        private Integer applicationId;

        private String name;

        @Schema(description = "虚拟资源", example = "true")
        @Builder.Default
        private Boolean virtualResource = false;

        private String resourceType;

        private Integer businessId;

        private Integer businessType;

        private String comment;

    }

}