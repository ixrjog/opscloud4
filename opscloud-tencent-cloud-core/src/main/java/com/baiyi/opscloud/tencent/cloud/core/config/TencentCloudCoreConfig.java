package com.baiyi.opscloud.tencent.cloud.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/20 4:23 下午
 * @Version 1.0
 */
@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "tencent.cloud", ignoreInvalidFields = true)
public class TencentCloudCoreConfig {

    private TencentCloudAccount account;

    @Data
    public static class TencentCloudAccount {

        private String uid;
        private String secretId;
        private String secretKey;
        private String regionId;
        private List<String> zones;

    }
}
