package com.baiyi.opscloud.sshserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/10/19 3:14 下午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "ssh.arthas", ignoreInvalidFields = true)
public class SshServerArthasConfig {

    private String server;
    private String kubernetes;

}
