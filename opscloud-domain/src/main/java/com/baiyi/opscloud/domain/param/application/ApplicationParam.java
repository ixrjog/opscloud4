package com.baiyi.opscloud.domain.param.application;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2021/7/12 1:10 下午
 * @Version 1.0
 */
public class ApplicationParam {

    @SuperBuilder(toBuilder = true)
    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ApplicationPageQuery extends SuperPageParam implements IExtend {

        @Schema(name = "应用名称")
        private String queryName;

        @Schema(name = "标签ID")
        private Integer tagId;

        @Schema(name = "展开")
        private Boolean extend;

    }

    @SuperBuilder(toBuilder = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class GetApplicationKubernetes implements IExtend {

        @Schema(name = "应用ID")
        @NotNull(message = "必须指定应用ID")
        @Min(value = 0, message = "应用ID不能为空")
        private Integer applicationId;

        @Builder.Default
        private Boolean extend = false;

        @Schema(name = "环境类型")
        private Integer envType;

    }

    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class UserPermissionApplicationPageQuery extends PageParam implements IExtend {

        @Schema(name = "应用名称")
        private String queryName;

        @Schema(name = "用户ID", example = "1")
        private Integer userId;

        @Schema(name = "展开")
        private Boolean extend;

    }

    @Data
    public static class Query {

        @NotNull
        @Schema(name = "应用ID")
        private Integer applicationId;

    }

}
