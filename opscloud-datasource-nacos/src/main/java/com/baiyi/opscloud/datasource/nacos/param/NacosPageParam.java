package com.baiyi.opscloud.datasource.nacos.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/11/12 3:56 下午
 * @Version 1.0
 */
public class NacosPageParam {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery {
        @Builder.Default
        private Integer pageNo = 1;
        @Builder.Default
        private Integer pageSize = 100;
        @ApiModelProperty(value = "token")
        private String accessToken;
    }
}
