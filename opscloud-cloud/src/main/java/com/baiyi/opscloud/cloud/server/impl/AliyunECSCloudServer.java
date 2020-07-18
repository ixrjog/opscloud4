package com.baiyi.opscloud.cloud.server.impl;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.opscloud.aliyun.ecs.AliyunECS;
import com.baiyi.opscloud.cloud.server.ICloudServer;
import com.baiyi.opscloud.cloud.server.builder.CloudServerBuilder;
import com.baiyi.opscloud.cloud.server.decorator.AliyunECSInstanceDecorator;
import com.baiyi.opscloud.cloud.server.instance.AliyunECSInstance;
import com.baiyi.opscloud.common.base.CloudServerPowerStatus;
import com.baiyi.opscloud.common.base.CloudServerType;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudServer;
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
@Component("AliyunECSCloudServer")
public class AliyunECSCloudServer<T> extends BaseCloudServer<T> implements ICloudServer {

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
    protected int getCloudServerType() {
        return CloudServerType.ECS.getType();
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
    protected OcCloudServer getCloudServer(T instance) {
        if (!(instance instanceof AliyunECSInstance)) return null;
        AliyunECSInstance i = (AliyunECSInstance) instance;
        return CloudServerBuilder.build(i, getInstanceDetail(instance));
    }

    @Override
    protected BusinessWrapper<Boolean> power(OcCloudServer ocCloudServer, Boolean action) {
        return aliyunECS.power(ocCloudServer.getRegionId(), ocCloudServer.getInstanceId(), action);
    }

    protected int getPowerStatus(String regionId, String instanceId) {
        DescribeInstancesResponse.Instance instance = aliyunECS.getInstance(regionId, instanceId);
        if (instance == null)
            return -1;
        instance.getStatus();
        return getPowerStatus(instance.getStatus());
    }

    private int getPowerStatus(String status) {
        if ("Running".equalsIgnoreCase(status))
            return CloudServerPowerStatus.RUNNING.getStatus();
        if ("Stopped".equalsIgnoreCase(status))
            return CloudServerPowerStatus.STOPPED.getStatus();
        if ("Starting".equalsIgnoreCase(status))
            return CloudServerPowerStatus.STARTING.getStatus();
        if ("Stopping".equalsIgnoreCase(status))
            return CloudServerPowerStatus.STOPPING.getStatus();
        return -1;
    }

    @Override
    protected Boolean delete(OcCloudServer ocCloudServer) {
        return aliyunECS.delete(ocCloudServer.getRegionId(), ocCloudServer.getInstanceId());
    }
}
