package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/11/29 3:25 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DingtalkConfig extends BaseDsConfig {

    private Dingtalk dingtalk;

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Dingtalk {

        private String version;
        private String url;
        private String company;
        private String corpId;
        private App app;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
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
    @ApiModel
    public static class Department {
        private Set<Long> deptIds;
    }

}
