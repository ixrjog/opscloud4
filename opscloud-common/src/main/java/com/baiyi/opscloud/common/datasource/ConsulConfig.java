package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.alert.AlertRuleMatchExpression;
import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/7/14 14:25
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConsulConfig extends BaseDsConfig {

    private Consul consul;

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Consul {

        private String version;

        private String url;
        // DataCenter
        private List<String> dcs;

        private List<AlertRuleMatchExpression> strategyMatchExpressions;

        private String dingtalkToken;

        private String ttsCode;

        private String templateCode;

    }

}

