package com.baiyi.opscloud.vmware.vcsa.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/1/3 3:34 下午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "vcsa", ignoreInvalidFields = true)
public class VcsaConfig {

    private String host;
    private String user;
    private String password;
    private String zone = "office";

}
