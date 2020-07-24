package com.baiyi.opscloud.jumpserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/1/9 11:29 上午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "jumpserver", ignoreInvalidFields = true)
public class JumpserverConfig {

    private String version = "1.5.6";
    private String url;
    private String coco;
    private JumpserverAdmin admin;


    @Data
    public static class JumpserverAdmin {

        private String username;
        private String password;
    }


}
