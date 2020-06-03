package com.baiyi.opscloud.cloud.server.impl;

import com.amazonaws.services.ec2.model.Instance;
import com.baiyi.opscloud.aws.ec2.AwsEC2;
import com.baiyi.opscloud.cloud.server.ICloudServer;
import com.baiyi.opscloud.cloud.server.builder.CloudServerBuilder;
import com.baiyi.opscloud.cloud.server.decorator.EC2InstanceDecorator;
import com.baiyi.opscloud.cloud.server.instance.AwsEC2Instance;
import com.baiyi.opscloud.cloud.server.util.AwsUtils;
import com.baiyi.opscloud.common.base.CloudServerType;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/1/13 11:38 上午
 * @Version 1.0
 */
@Slf4j
@Component("AwsEC2CloudServer")
public class AwsEC2CloudServer<T> extends BaseCloudServer<T> implements ICloudServer {

    @Resource
    private AwsEC2 awsEC2;

    @Resource
    private EC2InstanceDecorator ec2InstanceDecorator;

    @Override
    protected List<T> getInstanceList() {
        return getInstanceList(awsEC2.getInstanceList());
    }

    private List<T> getInstanceList(List<Instance> instanceList) {
        return instanceList.stream().map(e -> (T) ec2InstanceDecorator.decorator(e)).collect(Collectors.toList());
    }

    @Override
    protected String getInstanceId(T instance) throws Exception {
        if (!(instance instanceof AwsEC2Instance)) throw new Exception();
        AwsEC2Instance i = (AwsEC2Instance) instance;
        return i.getInstance().getInstanceId();
    }

    @Override
    protected T getInstance(String regionId, String instanceId) {
        return (T) ec2InstanceDecorator.decorator(awsEC2.getInstance(instanceId));
    }

    @Override
    protected String getInstanceName(T instance) {
        if (!(instance instanceof AwsEC2Instance)) return null;
        AwsEC2Instance i = (AwsEC2Instance) instance;
        return AwsUtils.getEC2InstanceName(i.getInstance());
    }

    @Override
    protected OcCloudServer getCloudServer(T instance) {
        if (!(instance instanceof AwsEC2Instance)) return null;
        AwsEC2Instance i = (AwsEC2Instance) instance;
        return CloudServerBuilder.build(i, getInstanceDetail(instance));
    }

    protected int getPowerStatus(String regionId, String instanceId) {
       return -1;
    }

    @Override
    protected int getCloudServerType() {
        return CloudServerType.EC2.getType();
    }

}
