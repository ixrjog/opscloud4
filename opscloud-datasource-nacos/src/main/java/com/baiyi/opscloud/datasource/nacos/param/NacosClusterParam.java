package com.baiyi.opscloud.datasource.nacos.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/11/12 10:31 上午
 * @Version 1.0
 */
public class NacosClusterParam {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class NodesQuery {
        @Builder.Default
        private Boolean withInstances = false;
        @Builder.Default
        private Integer pageNo = 1;
        @Builder.Default
        private Integer pageSize = 10;
        @Builder.Default
        private String keyword = "";
        @Schema(description = "token")
        private String accessToken;
        @Builder.Default
        private String namespaceId = "dev";
    }

}