package com.baiyi.opscloud.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/13 3:46 下午
 * @Since 1.0
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka", ignoreInvalidFields = true)
public class KafkaConfig {

    private List<KafkaInstance> instances;

    @Data
    public static class KafkaInstance {
        private String instanceName;
        private String instanceId;
        private Integer instanceType;
        private String regionId;
    }

}
