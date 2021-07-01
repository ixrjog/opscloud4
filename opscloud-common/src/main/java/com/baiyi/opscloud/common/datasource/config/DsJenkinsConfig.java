package com.baiyi.opscloud.common.datasource.config;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/7/1 1:52 下午
 * @Version 1.0
 */
public class DsJenkinsConfig {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Jenkins {

        private String version;
        private String url;
        private String username;
        private String token;

    }
}
