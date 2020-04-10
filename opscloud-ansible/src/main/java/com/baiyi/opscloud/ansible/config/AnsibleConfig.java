package com.baiyi.opscloud.ansible.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/4/6 5:28 下午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "ansible", ignoreInvalidFields = true)
public class AnsibleConfig {

    private String bin;
    private String playbookBin;
    private String logPath;

}
