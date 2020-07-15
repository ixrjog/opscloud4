package com.baiyi.opscloud.domain.param.kubernetes;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2020/7/1 1:49 下午
 * @Version 1.0
 */
public class KubernetesServiceParam {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "命名空间id", example = "1")
        private Integer namespaceId;

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

        @ApiModelProperty(value = "端口查询")
        private Integer queryPort;

        @ApiModelProperty(value = "扩展属性", example = "1")
        private Integer extend;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class QueryParam {

        @ApiModelProperty(value = "实例id", example = "1")
        @NotNull
        private Integer instanceId;

        @ApiModelProperty(value = "扩展属性", example = "1")
        private Integer extend;

    }

}
