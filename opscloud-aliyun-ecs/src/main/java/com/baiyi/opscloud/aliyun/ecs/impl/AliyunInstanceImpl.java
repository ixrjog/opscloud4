package com.baiyi.opscloud.aliyun.ecs.impl;

import com.aliyuncs.ecs.model.v20140526.DescribeInstanceTypesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeZonesResponse;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.ecs.AliyunInstance;
import com.baiyi.opscloud.aliyun.ecs.base.AliyunInstanceTypeVO;
import com.baiyi.opscloud.aliyun.ecs.handler.AliyunInstanceHandler;
import com.baiyi.opscloud.aliyun.ecs.handler.AliyunVPCHandler;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

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
        Map<String, Set<String>> map = Maps.newHashMap();
        for (DescribeZonesResponse.Zone zone : list) {
            for (String type : zone.getAvailableInstanceTypes()) {
                if (map.containsKey(type)) {
                    map.get(type).add(zone.getZoneId());
                } else {
                    Set<String> set = Sets.newHashSet();
                    set.add(zone.getZoneId());
                    map.put(type, set);
                }
            }
        }
        return map;
    }

    @Override
    public List<DescribeVSwitchesResponse.VSwitch> getVSwitchList(String regionId, String vpcId) {
        return aliyunVPCHandler.getVSwitchList(regionId, vpcId);
    }

}
