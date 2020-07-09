package com.baiyi.opscloud.domain.param.kubernetes;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/6/24 4:31 下午
 * @Version 1.0
 */
public class KubernetesClusterParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

        @ApiModelProperty(value = "扩展属性", example = "1")
        private Integer extend;

    }
}
