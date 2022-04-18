package com.baiyi.opscloud.datasource.aws.domain.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.route53.AmazonRoute53;
import com.amazonaws.services.route53.AmazonRoute53ClientBuilder;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.core.helper.AwsCredentialsHelper;

/**
 * @Author baiyi
 * @Date 2022/4/18 17:00
 * @Version 1.0
 */
public class AmazonRoute53Service {

    private AmazonRoute53Service() {
    }

    public static AmazonRoute53 buildAmazonRoute53(AwsConfig.Aws aws, String regionId) {
        AWSCredentials credentials = AwsCredentialsHelper.buildAWSCredentials(aws);
        return AmazonRoute53ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(regionId)
                .build();
    }

}
