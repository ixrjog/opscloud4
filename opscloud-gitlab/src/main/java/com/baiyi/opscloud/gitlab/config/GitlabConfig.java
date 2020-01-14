package com.baiyi.opscloud.gitlab.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/1/14 4:17 下午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "gitlab", ignoreInvalidFields = true)
public class GitlabConfig {

    private String url;
    private String token;

}
