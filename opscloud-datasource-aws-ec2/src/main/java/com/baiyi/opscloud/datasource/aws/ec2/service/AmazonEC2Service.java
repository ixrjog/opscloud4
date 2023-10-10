package com.baiyi.opscloud.datasource.aws.ec2.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.core.helper.AwsCredentialsManager;

/**
 * @Author baiyi
 * @Date 2022/1/21 11:02 AM
 * @Version 1.0
 */
public class AmazonEC2Service {

    public static AmazonEC2 buildAmazonEC2(AwsConfig.Aws aws) {
        return buildAmazonEC2(aws, aws.getRegionId());
    }

    public static AmazonEC2 buildAmazonEC2(AwsConfig.Aws aws, String regionId) {
        AWSCredentials credentials = AwsCredentialsManager.buildAWSCredentials(aws);
        // AWSCredentials credentials = awsCore.getAWSCredentials();
        return AmazonEC2ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(regionId)
                .build();
    }

}
