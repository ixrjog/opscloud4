package com.baiyi.opscloud.domain.param.kubernetes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2020/7/4 3:52 下午
 * @Version 1.0
 */
public class KubernetesPodParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class QueryParam {
        @ApiModelProperty(value = "实例id", example = "1")
        @NotNull
        private Integer instanceId;
    }
}
