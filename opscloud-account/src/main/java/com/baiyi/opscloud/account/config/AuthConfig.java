package com.baiyi.opscloud.account.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/5/26 6:08 下午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "auth", ignoreInvalidFields = true)
public class AuthConfig {

    private String externalAuthentication; // 外部认证
    private Admin admin;

    @Data
    public static class Admin {
        private String username;
        private String password;
    }

}
