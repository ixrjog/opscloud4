package com.baiyi.opscloud.kafka.handler;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.IAcsClient;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.kafka.config.KafkaConfig;
import com.baiyi.opscloud.kafka.helper.KafkaClientHelper;
import com.google.common.base.Joiner;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/14 5:47 下午
 * @Since 1.0
 */
public class AliyunKafkaHandler {

    private static final String DOMAIN_PREFIX = "alikafka";
    private static final String DOMAIN_SUFFIX = "aliyuncs.com";
    private static final String VERSION = "2019-09-16";

    @Resource
    private KafkaClientHelper kafkaClientHelper;

    @Resource
    private AliyunCore aliyunCore;

    private String getKafkaDomain(String instanceName) {
        String regionId = getKafkaInstance(instanceName).getRegionId();
        return Joiner.on(".").join(DOMAIN_PREFIX, regionId, DOMAIN_SUFFIX);
    }

    public IAcsClient getIAcsClient(String instanceName) {
        return aliyunCore.getAcsClient(getKafkaInstance(instanceName).getRegionId());
    }

    private KafkaConfig.KafkaInstance getKafkaInstance(String instanceName) {
        return kafkaClientHelper.getKafkaInstance(instanceName);
    }

    public CommonRequest getCommonRequest(String instanceName) {
        KafkaConfig.KafkaInstance instance = getKafkaInstance(instanceName);
        CommonRequest request = new CommonRequest();
        request.setSysVersion(VERSION);
        request.setSysDomain(getKafkaDomain(instanceName));
        request.putQueryParameter("RegionId", instance.getRegionId());
        request.putQueryParameter("InstanceId", instance.getInstanceId());
        return request;
    }


}
