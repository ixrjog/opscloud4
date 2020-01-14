package com.baiyi.opscloud.aws.core.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/1/13 11:11 上午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "aws", ignoreInvalidFields = true)
public class AwsConfig {

    private String uid;
    private String accessKeyId;
    private String secretKey;
    private String apRegionId;

    /**
     * 自定义配置
     */
    private Map<String, String> custom;

    public static final String EC2_INSTANCES_JSON = "ec2InstancesJson";

    public String getCustomByKey(String key) {
        return custom.get(key);
    }

    public AWSCredentials buildAWSCredentials() {
        return new BasicAWSCredentials(accessKeyId, secretKey);
    }


}
