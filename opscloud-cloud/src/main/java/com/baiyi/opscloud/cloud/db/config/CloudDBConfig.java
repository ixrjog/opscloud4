package com.baiyi.opscloud.cloud.db.config;

import com.baiyi.opscloud.aliyun.core.config.AliyunAccount;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/2 5:10 下午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "cloud.db", ignoreInvalidFields = true)
public class CloudDBConfig {

//    cloud:
//    db:
//    account:
//    prefix: oc_account
//    type: Normal

    private List<AliyunAccount> accounts;
}
