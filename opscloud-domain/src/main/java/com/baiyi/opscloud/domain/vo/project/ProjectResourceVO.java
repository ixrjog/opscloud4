package com.baiyi.opscloud.domain.vo.project;

import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @Author 修远
 * @Date 2023/5/15 6:41 PM
 * @Since 1.0
 */
public class ProjectResourceVO {

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Resource extends BaseVO {

        @Schema(description = "主键", example = "1")
        private Integer id;

        @NotNull(message = "项目ID不能为空")
        private Integer projectId;

        private String name;

        @Schema(description = "虚拟资源", example = "true")
        @Builder.Default
        private Boolean virtualResource = false;

        @NotNull(message = "资源类型不能为空")
        private String resourceType;

        @NotNull(message = "业务id不能为空")
        private Integer businessId;

        @NotNull(message = "业务类型不能为空")
        private Integer businessType;

        private String comment;

        private DsAssetVO.Asset asset;

        private Application application;

    }

}