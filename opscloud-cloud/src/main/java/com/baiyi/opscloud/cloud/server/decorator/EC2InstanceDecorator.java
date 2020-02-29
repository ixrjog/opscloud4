package com.baiyi.opscloud.cloud.server.decorator;

import com.baiyi.opscloud.aws.ec2.AwsEC2;
import com.baiyi.opscloud.cloud.server.instance.AwsEC2Instance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/13 1:23 下午
 * @Version 1.0
 */
@Component("AwsEC2InstanceDecorator")
public class EC2InstanceDecorator {

    @Resource
    private AwsEC2 awsEC2;

    public AwsEC2Instance decorator(com.amazonaws.services.ec2.model.Instance instance) {
        return AwsEC2Instance.builder()
                .instance(instance)
                .instanceType(awsEC2.getInstanceType(instance.getInstanceType()))
                .volumeList(awsEC2.getVolumeList(instance))
                .build();
    }

}
