package com.baiyi.opscloud.domain.param.template;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2021/12/6 11:00 AM
 * @Version 1.0
 */
public class BusinessTemplateParam {

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class BusinessTemplatePageQuery extends PageParam implements IExtend {

        @Schema(description = "实例UUID")
        @NotNull(message = "必须指定实例UUID")
        private String instanceUuid;

        @Schema(description = "关键字查询")
        private String queryName;

        @Schema(description = "环境类型")
        private Integer envType;

        @Schema(description = "展开数据")
        private Boolean extend;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class BusinessTemplate {

        private Integer id;
        @Schema(description = "实例UUID")
        @NotBlank(message = "必须指定实例UUID")
        private String instanceUuid;
        @Schema(description = "环境类型")
        private Integer envType;
        @Schema(description = "业务模板名称，不填写则自动生成")
        private String name;
        private Integer businessType;
        private Integer businessId;
        @NotNull(message = "必须指定模板")
        private Integer templateId;
        private String vars;
        private String content;
        private String comment;

    }

}