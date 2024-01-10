package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/11/29 3:25 下午
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingtalkConfig extends BaseDsConfig {

    private Dingtalk dingtalk;

    private Robot robot;

    @Data
    @NoArgsConstructor
    @Schema
    public static class Robot {

        private String token;
        private String desc;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Dingtalk {

        private String version;
        private String url;
        private String company;
        private String corpId;
        private App app;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class App {
        private String name;
        private String agentId;
        private String miniAppId;
        private String appKey;
        private String appSecret;
        private Department department;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Department {
        private Set<Long> deptIds;
    }

}