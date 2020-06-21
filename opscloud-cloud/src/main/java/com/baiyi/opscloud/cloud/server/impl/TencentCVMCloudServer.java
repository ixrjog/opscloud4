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
import com.baiyi.opscloud.tencent.cloud.cvm.handler.TencentCloudCVMHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/6/20 5:00 下午
 * @Version 1.0
 */
@Slf4j
@Component("TencentCVMCloudServer")
public class TencentCVMCloudServer<T> extends BaseCloudServer<T> implements ICloudServer {


    @Resource
    private TencentCloudCVMHandler tencentCloudCVMHandler;

    @Resource
    private AliyunECSInstanceDecorator aliyunECSInstanceDecorator;

    @Override
    protected List<T> getInstanceList() {
        return getInstanceList(tencentCloudCVMHandler.getInstanceList());
    }

    @Override
    protected T getInstance(String regionId, String instanceId) {
        return (T) aliyunECSInstanceDecorator.decorator(aliyunECS.getInstance(regionId, instanceId));
    }

    private List<T> getInstanceList(List<com.tencentcloudapi.cvm.v20170312.models.Instance> instanceList) {
        return (List<T>) instanceList;
    }

    @Override
    protected int getCloudServerType() {
        return CloudServerType.CVM.getType();
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
    protected BusinessWrapper<Boolean> power(OcCloudServer ocCloudserver, Boolean action) {
        return aliyunECS.power(ocCloudserver.getRegionId(), ocCloudserver.getInstanceId(), action);
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
}
