package com.baiyi.opscloud.domain.param.kubernetes;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/6/30 1:49 下午
 * @Version 1.0
 */
public class KubernetesTemplateParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "环境类型", example = "1")
        private Integer envType;

        @ApiModelProperty(value = "模版类型")
        private String templateType;

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

    }
}
