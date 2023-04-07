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

        @Schema(name = "名称")
        private String queryName;

        @Schema(name = "有效")
        private Boolean isActive;

        @Schema(name = "标签ID")
        private Integer tagId;

        @Schema(name = "数据源实例UUID")
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
        @Schema(name = "名称")
        private String name;

        @Schema(name = "实例UUID")
        private String jenkinsInstanceUuid;

        @Schema(name = "模板名称")
        private String templateName;

        @NotEmpty(message = "模板配置不能为空")
        @Schema(name = "模板配置")
        private String templateConfig;

        @Schema(name = "模板参数")
        private String templateParameter;

        @Schema(name = "模板内容")
        private String templateContent;

        @Schema(name = "有效")
        private Boolean isActive;

        @Schema(name = "描述")
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
        @Schema(name = "名称")
        private String name;

        @Schema(name = "实例UUID")
        private String jenkinsInstanceUuid;

        @Schema(name = "模板名称")
        private String templateName;

        @NotEmpty(message = "模板配置不能为空")
        @Schema(name = "模板配置")
        private String templateConfig;

        @Schema(name = "模板参数")
        private String templateParameter;

        @Schema(name = "模板内容")
        private String templateContent;

        @Schema(name = "有效")
        private Boolean isActive;

        @Schema(name = "描述")
        private String comment;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UpgradeJobTemplate {

        @NotNull(message = "模板ID不能为空")
        @Schema(name = "模板ID")
        private Integer templateId;

    }

}
