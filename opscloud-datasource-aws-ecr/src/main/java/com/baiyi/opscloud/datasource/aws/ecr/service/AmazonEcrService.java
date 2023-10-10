package com.baiyi.opscloud.datasource.aws.ecr.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.ecr.AmazonECR;
import com.amazonaws.services.ecr.AmazonECRClientBuilder;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.core.helper.AwsCredentialsManager;

/**
 * @Author baiyi
 * @Date 2022/8/16 16:44
 * @Version 1.0
 */
public class AmazonEcrService {

    private AmazonEcrService() {
    }

    public static AmazonECR buildAmazonECR(AwsConfig.Aws aws) {
        AWSCredentials credentials = AwsCredentialsManager.buildAWSCredentials(aws);
        return AmazonECRClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(aws.getRegionId())
                .build();
    }

    public static AmazonECR buildAmazonECR(String regionId, AwsConfig.Aws aws) {
        AWSCredentials credentials = AwsCredentialsManager.buildAWSCredentials(aws);
        return AmazonECRClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(regionId)
                .build();
    }

}
