package com.baiyi.opscloud.datasource.nacos.param;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema
    public static class PageQuery {
        @Builder.Default
        private Integer pageNo = 1;
        @Builder.Default
        private Integer pageSize = 100;
        @Schema(description = "token")
        private String accessToken;
    }

}