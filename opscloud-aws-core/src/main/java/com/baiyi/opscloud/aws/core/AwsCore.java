package com.baiyi.opscloud.aws.core;

import com.amazonaws.auth.AWSCredentials;

/**
 * @Author baiyi
 * @Date 2020/1/13 10:20 上午
 * @Version 1.0
 */
public interface AwsCore {

    AWSCredentials getAWSCredentials();

    String getApRegionId();

    String getCustomByKey(String key);
}
