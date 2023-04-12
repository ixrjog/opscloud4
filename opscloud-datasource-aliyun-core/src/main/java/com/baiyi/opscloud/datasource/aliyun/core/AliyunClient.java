package com.baiyi.opscloud.datasource.aliyun.core;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/6/18 9:52 上午
 * @Version 1.0
 */
@Component
public class AliyunClient extends SimpleAliyunClient {

    @Retryable(retryFor = ClientException.class, maxAttempts = 4, backoff = @Backoff(delay = 3000, multiplier = 1.5))
    public <T extends AcsResponse> T getAcsResponse(String regionId, AliyunConfig.Aliyun aliyun, AcsRequest<T> describe) throws ClientException {
        IAcsClient client = buildAcsClient(regionId, aliyun);
        return client.getAcsResponse(describe);
    }

}