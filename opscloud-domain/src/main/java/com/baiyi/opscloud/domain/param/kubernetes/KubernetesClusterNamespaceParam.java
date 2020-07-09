package com.baiyi.opscloud.domain.param.kubernetes;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/6/28 3:09 下午
 * @Version 1.0
 */
public class KubernetesClusterNamespaceParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "集群id", example = "1")
        private Integer clusterId;

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

        @ApiModelProperty(value = "扩展属性", example = "1")
        private Integer extend;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ExcludeQuery extends PageParam {

        @ApiModelProperty(value = "集群id", example = "1")
        private Integer clusterId;

    }

}
