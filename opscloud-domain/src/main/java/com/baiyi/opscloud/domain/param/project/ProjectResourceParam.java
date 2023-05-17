package com.baiyi.opscloud.domain.param.project;

import com.baiyi.opscloud.domain.base.BaseProjectResource;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2023/5/17 16:41
 * @Version 1.0
 */
public class ProjectResourceParam {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Resource implements BaseProjectResource.IProjectResource {

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

    }

}
