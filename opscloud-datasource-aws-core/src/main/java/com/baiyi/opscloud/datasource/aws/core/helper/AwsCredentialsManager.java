package com.baiyi.opscloud.datasource.aws.core.helper;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.baiyi.opscloud.common.datasource.AwsConfig;

/**
 * @Author baiyi
 * @Date 2022/1/21 10:55 AM
 * @Version 1.0
 */

public class AwsCredentialsManager {

    public static AWSCredentials buildAWSCredentials(AwsConfig.Aws aws) {
        return new BasicAWSCredentials(aws.getAccount().getAccessKeyId(), aws.getAccount().getSecretKey());
    }

}