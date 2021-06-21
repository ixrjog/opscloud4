package com.baiyi.caesar.datasource.aliyun.ecs.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.caesar.common.datasource.config.AliyunDsConfig;
import com.baiyi.caesar.datasource.aliyun.ecs.common.BaseAliyunHandler;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/6/18 9:52 上午
 * @Version 1.0
 */
@Component
public class AliyunInstanceHandler extends BaseAliyunHandler {

    @Retryable(value = ClientException.class, maxAttempts = 4, backoff = @Backoff(delay = 3000, multiplier = 1.5))
    public DescribeInstancesResponse getInstancesResponse(String regionId, AliyunDsConfig.Aliyun aliyun, DescribeInstancesRequest describe) throws ClientException {
        IAcsClient client = buildAcsClient(regionId, aliyun);
        return client.getAcsResponse(describe);
    }
}
