package com.baiyi.opscloud.domain.param.template;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

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
    @ApiModel
    public static class BusinessTemplatePageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "实例UUID")
        @NotNull(message = "必须指定实例UUID")
        private String instanceUuid;

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

        @ApiModelProperty(value = "环境类型")
        private Integer envType;

        @ApiModelProperty(value = "展开数据")
        private Boolean extend;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class BusinessTemplate {

        private Integer id;

        @ApiModelProperty(value = "实例UUID")
        @NotNull(message = "必须指定实例UUID")
        private String instanceUuid;

        @ApiModelProperty(value = "环境类型")
        private Integer envType;

        @ApiModelProperty(value = "业务模板名称，不填写则自动生成")
        private String name;

        private Integer businessType;

        private Integer businessId;

        private Integer templateId;

        private String vars;

        private String content;

        private String comment;

    }

}
