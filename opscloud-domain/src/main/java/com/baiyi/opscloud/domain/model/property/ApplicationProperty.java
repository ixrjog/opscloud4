package com.baiyi.opscloud.domain.model.property;

import com.baiyi.opscloud.domain.alert.AlertRuleMatchExpression;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/9/29 11:27 AM
 * @Since 1.0
 */
public class ApplicationProperty {

    @Data
    @NoArgsConstructor
    @Schema
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