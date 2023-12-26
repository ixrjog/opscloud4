package com.baiyi.opscloud.datasource.nexus.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2023/4/23 17:14
 * @Version 1.0
 */
public class SearchParam {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class SearchComponentsQuery implements IContinuationToken {

        @Schema(description = "组件仓库: maven-releases")
        private String repository;
        @Schema(description = "组件组")
        private String group;
        @Schema(description = "组件名")
        @Builder.Default
        private String name = "";
        @Schema(description = "组件版本")
        @Builder.Default
        private String version = "";

        @Schema(description = "连续查询")
        private String continuationToken;

    }

}