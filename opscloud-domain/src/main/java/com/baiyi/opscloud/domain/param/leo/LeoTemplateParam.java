package com.baiyi.opscloud.domain.param.leo;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;

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
    @ApiModel
    public static class TemplatePageQuery extends SuperPageParam implements IExtend {

        @ApiModelProperty(value = "名称")
        private String queryName;

        @ApiModelProperty(value = "有效")
        private Boolean isActive;

        @ApiModelProperty(value = "标签ID")
        private Integer tagId;

        @ApiModelProperty(value = "数据源实例UUID")
        private String instanceUuid;

        private final Integer businessType = BusinessTypeEnum.LEO_TEMPLATE.getType();

        private Boolean extend;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddTemplate {

        private Integer id;

        @NotEmpty(message = "名称不能为空")
        @ApiModelProperty(value = "名称")
        private String name;

        @ApiModelProperty(value = "实例UUID")
        private String jenkinsInstanceUuid;

        @ApiModelProperty(value = "模板名称")
        private String templateName;

        @NotEmpty(message = "模板配置不能为空")
        @ApiModelProperty(value = "模板配置")
        private String templateConfig;

        @ApiModelProperty(value = "模板参数")
        private String templateParameter;

        @ApiModelProperty(value = "模板内容")
        private String templateContent;

        @ApiModelProperty(value = "有效")
        private Boolean isActive;

        @ApiModelProperty(value = "描述")
        private String comment;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateTemplate {

        private Integer id;

        @NotEmpty(message = "名称不能为空")
        @ApiModelProperty(value = "名称")
        private String name;

        @ApiModelProperty(value = "实例UUID")
        private String jenkinsInstanceUuid;

        @ApiModelProperty(value = "模板名称")
        private String templateName;

        @NotEmpty(message = "模板配置不能为空")
        @ApiModelProperty(value = "模板配置")
        private String templateConfig;

        @ApiModelProperty(value = "模板参数")
        private String templateParameter;

        @ApiModelProperty(value = "模板内容")
        private String templateContent;

        @ApiModelProperty(value = "有效")
        private Boolean isActive;

        @ApiModelProperty(value = "描述")
        private String comment;

    }


}
