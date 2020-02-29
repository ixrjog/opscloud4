package com.baiyi.opscloud.cloud.server.decorator;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.opscloud.aliyun.ecs.AliyunECS;
import com.baiyi.opscloud.cloud.server.instance.AliyunECSInstance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/13 6:52 下午
 * @Version 1.0
 */
@Component("AliyunECSInstanceDecorator")
public class AliyunECSInstanceDecorator {

    @Resource
    private AliyunECS aliyunECS;

    public AliyunECSInstance decorator(DescribeInstancesResponse.Instance instance) {
        if (instance == null) return null;
        AliyunECSInstance aliyunECSInstance = AliyunECSInstance.builder()
                .instance(instance)
                .diskList(aliyunECS.getDiskList(instance.getRegionId(), instance.getInstanceId()))
                .build();
        if (instance.getInstanceChargeType().equalsIgnoreCase("PrePaid"))
            aliyunECSInstance.setRenewalStatus(aliyunECS.getRenewAttribute(instance.getRegionId(), instance.getInstanceId()));
        return aliyunECSInstance;
    }
}
