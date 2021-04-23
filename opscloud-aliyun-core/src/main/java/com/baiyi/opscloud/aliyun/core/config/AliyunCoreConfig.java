package com.baiyi.opscloud.aliyun.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/10 6:35 下午
 * @Version 1.0
 */
@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "aliyun", ignoreInvalidFields = true)
public class AliyunCoreConfig {

    private List<AliyunAccount> accounts;
    private Ons ons;

    @Data
    public static class AliyunAccount {

        private String uid;
        private Boolean master;
        private String name;
        private String accessKeyId;
        private String secret;
        private String regionId;
        private List<String> regionIds;
    }

    @Data
    public static class Ons {
        private String internetRegionId;
    }
}
