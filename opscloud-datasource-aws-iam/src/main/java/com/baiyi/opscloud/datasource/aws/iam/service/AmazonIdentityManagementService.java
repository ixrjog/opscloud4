package com.baiyi.opscloud.datasource.aws.iam.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.core.helper.AwsCredentialsManager;

/**
 * @Author baiyi
 * @Date 2022/1/21 2:07 PM
 * @Version 1.0
 */
public class AmazonIdentityManagementService {

    private AmazonIdentityManagementService() {
    }

    public static AmazonIdentityManagement buildAmazonIdentityManagement(AwsConfig.Aws aws) {
        AWSCredentials credentials = AwsCredentialsManager.buildAWSCredentials(aws);
        return AmazonIdentityManagementClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(aws.getRegionId())
                .build();
    }

}
