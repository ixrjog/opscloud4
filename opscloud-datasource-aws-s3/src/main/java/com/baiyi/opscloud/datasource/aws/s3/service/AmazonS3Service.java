package com.baiyi.opscloud.datasource.aws.s3.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.core.helper.AwsCredentialsManager;

/**
 * @Author 修远
 * @Date 2023/5/31 6:13 PM
 * @Since 1.0
 */
public class AmazonS3Service {

    private AmazonS3Service() {
    }

    public static AmazonS3 buildAmazonS3(String regionId, AwsConfig.Aws aws) {
        AWSCredentials credentials = AwsCredentialsManager.buildAWSCredentials(aws);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(regionId)
                .build();
    }

}
