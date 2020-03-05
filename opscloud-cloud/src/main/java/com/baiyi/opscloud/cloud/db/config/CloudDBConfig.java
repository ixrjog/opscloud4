package com.baiyi.opscloud.cloud.db.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/3/2 5:10 下午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "cloud.db", ignoreInvalidFields = true)
public class CloudDBConfig {

  private String prefix;
  private String type;

}
