package com.baiyi.opscloud.domain.param.kubernetes;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2020/7/2 2:13 下午
 * @Version 1.0
 */
public class KubernetesApplicationInstanceParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "应用id", example = "1")
        private Integer applicationId;

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

        @ApiModelProperty(value = "环境类型", example = "1")
        private Integer envType;

        @ApiModelProperty(value = "环境标签", example = "1")
        private String envLabel;

        @ApiModelProperty(value = "扩展属性", example = "1")
        @NotNull
        private Integer extend;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class LabelQuery {
        @ApiModelProperty(value = "环境类型", example = "1")
        @NotNull
        private Integer envType;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TemplatePageQuery extends KubernetesTemplateParam.PageQuery {

        @ApiModelProperty(value = "实例id", example = "1")
        @NotNull(message = "实例id不能为空")
        private Integer instanceId;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CreateByTemplate {

        @ApiModelProperty(value = "实例id", example = "1")
        @NotNull(message = "实例id不能为空")
        private Integer instanceId;

        @ApiModelProperty(value = "模版id", example = "1")
        @NotNull(message = "模版id不能为空")
        private Integer templateId;
    }
}
