package com.baiyi.opscloud.aws.ec2.context;


import com.baiyi.opscloud.aws.core.AwsCore;
import com.baiyi.opscloud.aws.core.config.AwsConfig;
import com.baiyi.opscloud.aws.ec2.base.EC2InstanceType;
import com.baiyi.opscloud.aws.ec2.mapper.EC2InstanceTypeMapper;
import com.baiyi.opscloud.common.util.JSONUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/1/13 2:13 下午
 * @Version 1.0
 */
@EnableCaching
@Component
public class EC2InstanceTypeContext {

    @Resource
    private AwsCore awsCore;

    @Cacheable(cacheNames = "instanceTypeContext",key="#root.targetClass")
    public Map<String, EC2InstanceType> getInstanceTypeContext() {
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(awsCore.getCustomByKey(AwsConfig.EC2_INSTANCES_JSON));
            HttpResponse response = httpClient.execute(httpGet, new HttpClientContext());
            HttpEntity entity = response.getEntity();
            JsonNode rootNode = JSONUtils.readTree(EntityUtils.toByteArray(entity));
            return new EC2InstanceTypeMapper().mapFromJson(rootNode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
