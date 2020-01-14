package com.baiyi.opscloud.cloudserver.impl;

import com.amazonaws.services.ec2.model.Instance;
import com.baiyi.opscloud.aws.ec2.AwsEC2;
import com.baiyi.opscloud.cloudserver.ICloudserver;
import com.baiyi.opscloud.cloudserver.base.CloudserverType;
import com.baiyi.opscloud.cloudserver.builder.OcCloudserverBuilder;
import com.baiyi.opscloud.cloudserver.decorator.EC2InstanceDecorator;
import com.baiyi.opscloud.cloudserver.instance.AwsEC2Instance;
import com.baiyi.opscloud.cloudserver.util.AwsUtils;
import com.baiyi.opscloud.domain.generator.OcCloudserver;
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
@Component("AwsEC2Cloudserver")
public class AwsEC2Cloudserver<T> extends BaseCloudserver<T> implements ICloudserver {

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
    protected OcCloudserver getCloudserver(T instance) {
        if (!(instance instanceof AwsEC2Instance)) return null;
        AwsEC2Instance i = (AwsEC2Instance) instance;
        return OcCloudserverBuilder.buildOcCloudserver(i, getInstanceDetail(instance));
    }


    @Override
    protected int getCloudserverType() {
        return CloudserverType.EC2.getType();
    }

}
