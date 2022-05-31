package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseConfig;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/22 1:38 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ZabbixConfig extends BaseConfig {

    private Zabbix zabbix;

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
        private List<String> severityTypes;
        // IP段范围
        private List<String> regions;
        private Notice notice;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Operation {
        private String subject;
        private String message;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Notice {
        private String media;
        private Integer priority;
        private String token;
    }

}

