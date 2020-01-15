package com.baiyi.opscloud.cloudserver.impl;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.opscloud.aliyun.ecs.AliyunECS;
import com.baiyi.opscloud.cloudserver.ICloudserver;
import com.baiyi.opscloud.cloudserver.base.CloudserverType;
import com.baiyi.opscloud.cloudserver.builder.OcCloudserverBuilder;
import com.baiyi.opscloud.cloudserver.decorator.AliyunECSInstanceDecorator;
import com.baiyi.opscloud.cloudserver.instance.AliyunECSInstance;
import com.baiyi.opscloud.domain.generator.OcCloudserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/1/10 7:12 下午
 * @Version 1.0
 */
@Slf4j
@Component("AliyunECSCloudserver")
public class AliyunECSCloudserver<T> extends BaseCloudserver<T> implements ICloudserver {

    @Resource
    private AliyunECS aliyunECS;

    @Resource
    private AliyunECSInstanceDecorator aliyunECSInstanceDecorator;

    @Override
    protected List<T> getInstanceList() {
        return getInstanceList(aliyunECS.getInstanceList());
    }

    @Override
    protected T getInstance(String regionId, String instanceId) {
        return (T) aliyunECSInstanceDecorator.decorator(aliyunECS.getInstance(regionId, instanceId));
    }


    private List<T> getInstanceList(List<DescribeInstancesResponse.Instance> instanceList) {
        return instanceList.stream().map(e -> (T) aliyunECSInstanceDecorator.decorator(e)).collect(Collectors.toList());
    }

    @Override
    protected int getCloudserverType() {
        return CloudserverType.ECS.getType();
    }

    @Override
    protected String getInstanceId(T instance) throws Exception {
        if (!(instance instanceof AliyunECSInstance)) throw new Exception();
        AliyunECSInstance i = (AliyunECSInstance) instance;
        return i.getInstance().getInstanceId();
    }

    @Override
    protected String getInstanceName(T instance) {
        if (!(instance instanceof AliyunECSInstance)) return null;
        AliyunECSInstance i = (AliyunECSInstance) instance;
        return i.getInstance().getInstanceName();
    }


    @Override
    protected OcCloudserver getCloudserver(T instance) {
        if (!(instance instanceof AliyunECSInstance)) return null;
        AliyunECSInstance i = (AliyunECSInstance) instance;
        return OcCloudserverBuilder.build(i, getInstanceDetail(instance));
    }

}
