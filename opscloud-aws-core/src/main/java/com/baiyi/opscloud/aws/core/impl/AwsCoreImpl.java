package com.baiyi.opscloud.aws.core.impl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.baiyi.opscloud.aws.core.AwsCore;
import com.baiyi.opscloud.aws.core.config.AwsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/13 10:21 上午
 * @Version 1.0
 */
@Slf4j
@Component("AwsCore")
public class AwsCoreImpl implements AwsCore, InitializingBean {

    @Resource
    private AwsConfig awsConfig;

    private AWSCredentials credentials;

    @Override
    public AWSCredentials getAWSCredentials() {
        return credentials;
    }

    @Override
    public String getCustomByKey(String key) {
        return awsConfig.getCustomByKey(key);
    }


    @Override
    public  String getApRegionId(){
       return  awsConfig.getApRegionId();
    }


    @Override
    public void afterPropertiesSet() throws Exception {

        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            /**
             * throw new AmazonClientException(
             * "Cannot load the credentials from the credential profiles file. " +
             * "Please make sure that your credentials file is at the correct " +
             * "location (~/.aws/credentials), and is in valid format.",
             * e);
             * 证书未找到，使用配置文件
             */
        }

        if (credentials == null) {
            try {
                credentials = awsConfig.buildAWSCredentials();
            } catch (Exception e) {
                log.error("AWS AccessKeyId/SecretKey 未配置！", e);
            }
        }
    }


}
