package com.baiyi.opscloud.domain.param.template;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2021/12/6 11:37 AM
 * @Version 1.0
 */
public class TemplateParam {

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class TemplatePageQuery extends PageParam implements IExtend {

        @Schema(description = "关键字查询")
        private String queryName;

        @Schema(description = "实例类型查询")
        private String instanceType;

        @Schema(description = "模板关键字查询")
        private String templateKey;

        @Schema(description = "模板类型查询")
        private String templateType;

        @Schema(description = "环境类型")
        private Integer envType;

        @Schema(description = "分类")
        private String kind;

        @Schema(description = "展开数据")
        private Boolean extend;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Template {

        private Integer id;
        @NotBlank(message = "必须指定模板名称")
        private String name;
        @NotNull(message = "必须指定环境类型")
        private Integer envType;
        @NotBlank(message = "必须指定实例类型")
        private String instanceType;
        @NotBlank(message = "必须指定模板Key")
        private String templateKey;
        @NotBlank(message = "必须指定模板格式类型")
        private String templateType;
        private String vars;
        private String kind;
        private String content;
        private String comment;

    }

}