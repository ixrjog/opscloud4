package com.baiyi.opscloud.aliyun.ecs.impl;

import com.aliyuncs.ecs.model.v20140526.*;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.ecs.AliyunInstance;
import com.baiyi.opscloud.aliyun.ecs.base.AliyunInstanceTypeVO;
import com.baiyi.opscloud.aliyun.ecs.handler.AliyunInstanceHandler;
import com.baiyi.opscloud.aliyun.ecs.handler.AliyunVPCHandler;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/3/20 1:12 下午
 * @Version 1.0
 */
@Component
public class AliyunInstanceImpl implements AliyunInstance {
    @Resource
    private AliyunCore aliyunCore;

    @Resource
    private AliyunInstanceHandler aliyunInstanceHandler;

    @Resource
    private AliyunVPCHandler aliyunVPCHandler;

    /**
     * key : instanceType = ecs.t1.xsmall
     *
     * @return
     */
    @Cacheable(cacheNames = "instanceTypeContext", key = "#root.targetClass")
    @Override
    public Map<String, AliyunInstanceTypeVO.InstanceType> getInstanceTypeMap() {
        List<DescribeInstanceTypesResponse.InstanceType> data = aliyunInstanceHandler.getInstanceTypeList(aliyunCore.getAccount().getRegionId());
        return data.stream().collect(Collectors.toMap(DescribeInstanceTypesResponse.InstanceType::getInstanceTypeId
                , a -> BeanCopierUtils.copyProperties(a, AliyunInstanceTypeVO.InstanceType.class), (k1, k2) -> k1));
    }

    @Cacheable(cacheNames = "instanceTypeContext", key = "#root.targetClass + ':' + #regionId")
    @Override
    public Map<String, Set<String>> getInstanceTypeZoneMap(String regionId) {
        List<DescribeZonesResponse.Zone> list
                = aliyunInstanceHandler.getZoneList(regionId);
        Map<String, Set<String>> zoneMap = Maps.newHashMap();
        list.forEach(zone -> zone.getAvailableInstanceTypes().forEach(t -> {
            if (zoneMap.containsKey(t)) {
                zoneMap.get(t).add(zone.getZoneId());
            } else {
                Set<String> set = Sets.newHashSet();
                set.add(zone.getZoneId());
                zoneMap.put(t, set);
            }
        }));
        return zoneMap;
    }

    @Override
    public BusinessWrapper<String> getCreateInstanceResponse(String regionId, CreateInstanceRequest createInstanceRequest) {
        return aliyunInstanceHandler.getCreateInstanceResponse(regionId, createInstanceRequest);
    }

    @Override
    @Retryable(value = Exception.class, maxAttempts = 4, backoff = @Backoff(delay = 3000, multiplier = 1.5))
    public DescribeInstancesResponse.Instance getStoppedInstance(String regionId, String hostname) throws Exception {
        List<DescribeInstancesResponse.Instance> instanceList = aliyunInstanceHandler.getStoppedInstance(regionId);
        if (CollectionUtils.isEmpty(instanceList))
            throw new Exception();
        for (DescribeInstancesResponse.Instance instance : instanceList) {
            if (instance.getHostName().equals(hostname))
                return instance;
        }
        throw new Exception();
    }

    @Override
    @Retryable(value = Exception.class, maxAttempts = 4, backoff = @Backoff(delay = 3000, multiplier = 1.5))
    public String allocateInstancePublicIp(String regionId, String instanceId) throws Exception {
        String publicIp = aliyunInstanceHandler.allocateInstancePublicIp(regionId, instanceId);
        if (StringUtils.isEmpty(publicIp))
            throw new Exception();
        return publicIp;
    }

    @Override
    @Retryable(value = Exception.class, maxAttempts = 4, backoff = @Backoff(delay = 3000, multiplier = 1.5))
    public boolean startInstance(String regionId, String instanceId) throws Exception {
        if (aliyunInstanceHandler.startInstance(regionId, instanceId))
            return true;
        throw new Exception();
    }

    @Override
    public List<DescribeVSwitchesResponse.VSwitch> getVSwitchList(String regionId, String vpcId) {
        return aliyunVPCHandler.getVSwitchList(regionId, vpcId);
    }

    @Override
    public List<DescribeInstancesResponse.Instance> getInstanceList(String regionId, List<String> instanceIds) {
        return aliyunInstanceHandler.getInstanceList(regionId, instanceIds);
    }


}
