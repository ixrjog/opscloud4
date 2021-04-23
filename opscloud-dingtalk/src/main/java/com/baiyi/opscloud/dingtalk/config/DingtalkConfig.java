package com.baiyi.opscloud.dingtalk.config;

import com.google.common.base.Joiner;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/13 6:15 下午
 * @Since 1.0
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "dingtalk", ignoreInvalidFields = true)
public class DingtalkConfig {

    private final static String ACCESS_TOKEN = "access_token";

    private String url;
    private String apiUrl;
    private List<DingtalkAccount> accounts;

    @Data
    public static class DingtalkAccount {
        private String uid;
        private Boolean master;
        private String name;
        private Long agentId;
        private String appKey;
        private String appSecret;
    }

    public String getWebHook(String dingtalkToken) {
        return Joiner.on("").join(url, "?", ACCESS_TOKEN, "=", dingtalkToken);
    }
}
