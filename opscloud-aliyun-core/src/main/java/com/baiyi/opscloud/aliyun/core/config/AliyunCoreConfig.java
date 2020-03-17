package com.baiyi.opscloud.aliyun.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/10 6:35 下午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun", ignoreInvalidFields = true)
public class AliyunCoreConfig {

    private List<AliyunAccount> accounts;

}
