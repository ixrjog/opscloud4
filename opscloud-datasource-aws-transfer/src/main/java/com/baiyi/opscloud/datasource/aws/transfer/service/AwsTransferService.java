package com.baiyi.opscloud.datasource.aws.transfer.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.transfer.AWSTransfer;
import com.amazonaws.services.transfer.AWSTransferClientBuilder;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.core.helper.AwsCredentialsManager;

/**
 * @Author baiyi
 * @Date 2023/10/9 17:20
 * @Version 1.0
 */
public class AwsTransferService {

    private AwsTransferService() {
    }

    public static AWSTransfer buildAwsTransfer(String regionId, AwsConfig.Aws aws) {
        AWSCredentials credentials = AwsCredentialsManager.buildAWSCredentials(aws);
        return AWSTransferClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(regionId)
                .build();
    }

}
