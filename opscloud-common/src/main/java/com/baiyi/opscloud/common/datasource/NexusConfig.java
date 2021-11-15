package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseConfig;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/5 5:47 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NexusConfig extends BaseConfig {

    private Nexus nexus;

    interface RepositoryType {
        String SNAPSHOT = "SNAPSHOT";
        String RELEASE = "RELEASE";
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Nexus {

        private String version;
        private String url;
        private String user;
        private String password;

        private List<Repository> repositories; // 需要同步的仓库列表

        private List<String> filter; // 保留扩展名资产
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Repository {
        private String name;
        private String kind;
    }

}

