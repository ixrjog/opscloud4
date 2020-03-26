package com.baiyi.opscloud.aliyun.ecs;

import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import com.baiyi.opscloud.aliyun.ecs.base.AliyunInstanceTypeVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/3/20 1:12 下午
 * @Version 1.0
 */
public interface AliyunInstance {

    Map<String, AliyunInstanceTypeVO.InstanceType> getInstanceTypeMap();

    /**
     * Key : instanceTypeId
     * Value : Set<String zoneId>
     * @param regionId
     * @return
     */
    Map<String, Set<String>> getInstanceTypeZoneMap(String regionId);

    List<DescribeVSwitchesResponse.VSwitch> getVSwitchList(String regionId, String vpcId);
}
