package com.baiyi.opscloud.domain.param.template;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModel
    public static class TemplatePageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

        @ApiModelProperty(value = "实例类型查询")
        private String instanceType;

        @ApiModelProperty(value = "模版关键字查询")
        private String templateKey;

        @ApiModelProperty(value = "模版类型查询")
        private String templateType;

        @ApiModelProperty(value = "环境类型")
        private Integer envType;

        @ApiModelProperty(value = "展开数据")
        private Boolean extend;
    }

}
