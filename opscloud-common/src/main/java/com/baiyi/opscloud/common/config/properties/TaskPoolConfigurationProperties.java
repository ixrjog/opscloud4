package com.baiyi.opscloud.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/4/27 10:10
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "task.pool", ignoreInvalidFields = true)
public class TaskPoolConfigurationProperties {

    private ThreadPool core;
    private ThreadPool leo;
    private ThreadPool st;
    private ThreadPool kt;

    @Data
    public static class ThreadPool {
        private Integer coreSize;
        private Integer maxSize;
    }

}