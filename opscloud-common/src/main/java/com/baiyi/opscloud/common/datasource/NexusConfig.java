package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/5 5:47 下午
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NexusConfig extends BaseDsConfig {

    private Nexus nexus;

    interface RepositoryType {
        String SNAPSHOT = "SNAPSHOT";
        String RELEASE = "RELEASE";
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Nexus {

        private String version;
        private String url;
        private String user;
        private String password;

        @Schema(description = "需要同步的仓库列表")
        private List<Repository> repositories;
        @Schema(description = "保留扩展名资产")
        private List<String> filter;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Repository {
        private String name;
        private String kind;
    }

}