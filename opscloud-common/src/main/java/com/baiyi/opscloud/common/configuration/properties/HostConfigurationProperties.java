package com.baiyi.opscloud.common.configuration.properties;

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
@ConfigurationProperties(prefix = "host", ignoreInvalidFields = true)
public class HostConfigurationProperties {

    private String url;

}
