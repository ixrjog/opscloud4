package com.baiyi.opscloud.datasource.aws.sqs.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.core.helper.AwsCredentialsManager;

/**
 * @Author baiyi
 * @Date 2022/3/28 18:11
 * @Version 1.0
 */
public class AmazonSNSService {

    private AmazonSNSService() {
    }

    public static AmazonSNS buildAmazonSNS(AwsConfig.Aws aws, String regionId) {
        AWSCredentials credentials = AwsCredentialsManager.buildAWSCredentials(aws);
        return AmazonSNSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(regionId)
                .build();
    }

}
