package com.baiyi.opscloud.datasource.aliyun.arms.client;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.arms20190808.AsyncClient;
import com.baiyi.opscloud.common.datasource.AliyunArmsConfig;
import darabonba.core.client.ClientOverrideConfiguration;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/6/27 09:39
 * @Version 1.0
 */
public class AliyunArmsClient {

    private static final String ARMS_ENDPOINT = "arms.cn-hangzhou.aliyuncs.com";

    private static final String REGION = "undefined";

    public static final Long MAX_RESULTS = 200L;

    public static final String MAX_RESULTS_S = String.valueOf(MAX_RESULTS);

    /**
     * 代码参考
     * https://next.api.aliyun.com/api-tools/sdk/ARMS?version=2019-08-08&language=java-async-tea
     *
     * 接入点参考
     * https://help.aliyun.com/document_detail/441765.html?spm=a2c4g.441908.0.0.8800710dLdFZHW
     *
     * @param regionId
     * @param arms
     * @return
     */
    public static AsyncClient buildAsyncClient(String regionId, AliyunArmsConfig.Arms arms) {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(arms.getAccount().getAccessKeyId())
                .accessKeySecret(arms.getAccount().getSecret())
                //.securityToken("<your-token>") // use STS token
                .build());

        String region = "cn-hangzhou";
        if (StringUtils.isNotBlank(regionId)) {
            region = regionId;
        }

        final String endpoint = Optional.of(arms)
                .map(AliyunArmsConfig.Arms::getEndpoint)
                .orElse(ARMS_ENDPOINT);

        ClientOverrideConfiguration clientOverrideConfiguration = ClientOverrideConfiguration.create()
                .setEndpointOverride(endpoint)
                .setConnectTimeout(Duration.ofSeconds(30));

        return AsyncClient.builder()
                // Region ID
                .region(region)
                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
                .credentialsProvider(provider)
                .overrideConfiguration(clientOverrideConfiguration)
                .build();
    }

}