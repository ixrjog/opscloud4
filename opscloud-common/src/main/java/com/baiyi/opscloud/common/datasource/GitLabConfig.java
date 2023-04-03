package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/6/21 4:57 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GitLabConfig extends BaseDsConfig {

    private Gitlab gitlab;

    @Data
    @NoArgsConstructor
    @Schema
    public static class Gitlab {
        private Api api;
        private String url;
        private String token;
        private SystemHooks systemHooks;

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
}

