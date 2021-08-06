package com.baiyi.opscloud.common.datasource.config;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/8/5 5:48 下午
 * @Version 1.0
 */
public class DsNexusConfig {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Nexus {
        private String version;
        private String url;
        private String user;
        private String password;
    }
}
