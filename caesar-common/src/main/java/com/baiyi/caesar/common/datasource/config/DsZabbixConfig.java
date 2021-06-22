package com.baiyi.caesar.common.datasource.config;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/6/22 1:39 下午
 * @Version 1.0
 */
public class DsZabbixConfig {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Zabbix {
        private String version;
        private String url;
        private String user;
        private String password;
        private String zone;
        private Operation operation;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Operation {
        private String subject;
        private String message;
    }
}
