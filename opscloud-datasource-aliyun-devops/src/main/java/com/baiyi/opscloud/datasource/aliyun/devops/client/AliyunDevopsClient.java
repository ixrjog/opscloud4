package com.baiyi.opscloud.datasource.aliyun.devops.client;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.devops20210625.AsyncClient;
import com.baiyi.opscloud.common.datasource.AliyunDevopsConfig;
import darabonba.core.client.ClientOverrideConfiguration;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author baiyi
 * @Date 2023/5/11 17:50
 * @Version 1.0
 */
public class AliyunDevopsClient {

    private static final String DEVOPS_ENDPOINT = "devops.cn-hangzhou.aliyuncs.com";

    public static final Long MAX_RESULTS = 200L;

    public static final String MAX_RESULTS_S = String.valueOf(MAX_RESULTS);

    public static AsyncClient buildAsyncClient(String regionId, AliyunDevopsConfig.Devops devops) {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(devops.getAccount().getAccessKeyId())
                .accessKeySecret(devops.getAccount().getSecret())
                //.securityToken("<your-token>") // use STS token
                .build());
        String region = "cn-hangzhou";
        if (StringUtils.isNotBlank(regionId)) {
            region = regionId;
        }

        //.serviceConfiguration(Configuration.create()) // Service-level configuration
        // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
        ClientOverrideConfiguration clientOverrideConfiguration = ClientOverrideConfiguration.create()
                .setEndpointOverride(DEVOPS_ENDPOINT);
        //.setConnectTimeout(Duration.ofSeconds(30))

        return AsyncClient.builder()
                // Region ID
                .region(region)
                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
                .credentialsProvider(provider)
                .overrideConfiguration(clientOverrideConfiguration)
                .build();
    }

}