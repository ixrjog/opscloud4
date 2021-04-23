package com.baiyi.opscloud.common.config;

import com.google.common.base.Joiner;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/3/2 9:59 上午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "prometheus", ignoreInvalidFields = true)
public class PrometheusConfig {

    private String metricsPathSuffix;
    private String configPathPrefix;
    // /data/opscloud-data/prometheus
    private String dataPath;

    public String buildConfigFilePath(String file) {
        return Joiner.on("/").join(dataPath, "config_files", file);
    }

}
