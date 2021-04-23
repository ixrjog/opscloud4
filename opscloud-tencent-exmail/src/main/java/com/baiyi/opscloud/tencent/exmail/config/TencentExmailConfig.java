package com.baiyi.opscloud.tencent.exmail.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/10/9 9:51 上午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "tencent.exmail", ignoreInvalidFields = true)
public class TencentExmailConfig {

    private String apiUrl;
    private List<Account> accounts;

    @Data
    public static class Account {
        private String corpId;
        private Boolean master;
        private String name;
        private String corpSecret;
    }
}
