package com.baiyi.opscloud.domain.param.leo;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2022/11/1 16:39
 * @Version 1.0
 */
public class LeoTemplateParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class TemplatePageQuery extends SuperPageParam implements IExtend {

        @Schema(description = "名称")
        private String queryName;

        @Schema(description = "有效")
        private Boolean isActive;

        @Schema(description = "标签ID")
        private Integer tagId;

        @Schema(description = "数据源实例UUID")
        private String instanceUuid;

        private final Integer businessType = BusinessTypeEnum.LEO_TEMPLATE.getType();

        private Boolean extend;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AddTemplate {

        private Integer id;

        @NotEmpty(message = "名称不能为空")
        @Schema(description = "名称")
        private String name;

        @Schema(description = "实例UUID")
        private String jenkinsInstanceUuid;

        @Schema(description = "模板名称")
        private String templateName;

        @NotEmpty(message = "模板配置不能为空")
        @Schema(description = "模板配置")
        private String templateConfig;

        @Schema(description = "模板参数")
        private String templateParameter;

        @Schema(description = "模板内容")
        private String templateContent;

        @Schema(description = "有效")
        private Boolean isActive;

        @Schema(description = "描述")
        private String comment;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UpdateTemplate {

        private Integer id;

        @NotEmpty(message = "名称不能为空")
        @Schema(description = "名称")
        private String name;

        @Schema(description = "实例UUID")
        private String jenkinsInstanceUuid;

        @Schema(description = "模板名称")
        private String templateName;

        @NotEmpty(message = "模板配置不能为空")
        @Schema(description = "模板配置")
        private String templateConfig;

        @Schema(description = "模板参数")
        private String templateParameter;

        @Schema(description = "模板内容")
        private String templateContent;

        @Schema(description = "有效")
        private Boolean isActive;

        @Schema(description = "描述")
        private String comment;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UpgradeJobTemplate {

        @NotNull(message = "模板ID不能为空")
        @Schema(description = "模板ID")
        private Integer templateId;

    }

}