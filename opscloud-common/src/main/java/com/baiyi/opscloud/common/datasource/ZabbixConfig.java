package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/22 1:38 下午
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ZabbixConfig extends BaseDsConfig {

    private Zabbix zabbix;

    @Data
    @NoArgsConstructor
    @Schema
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
    @Schema
    public static class Operation {
        private String subject;
        private String message;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Notice {
        private String media;
        private Integer priority;
        private String token;
    }

}