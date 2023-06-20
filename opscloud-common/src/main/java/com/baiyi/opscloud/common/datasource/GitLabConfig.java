package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/21 4:57 下午
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GitLabConfig extends BaseDsConfig {

    private GitLab gitlab;

    @Data
    @NoArgsConstructor
    @Schema
    public static class GitLab {

        private Api api;
        private String url;
        private String token;
        private SystemHooks systemHooks;
        private GitFlow gitFlow;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Api {

        private String version;
        private Integer connectTimeout;
        private Integer readTimeout;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class SystemHooks {
        private String token;  // 回调token
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class GitFlow {

        private Boolean enabled;
        @Schema(description = "环境分支限制")
        private Map<String, List<String>> filter;

    }

}