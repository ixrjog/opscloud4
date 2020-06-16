package com.baiyi.opscloud.aliyun.ecs;

import com.aliyuncs.ecs.model.v20140526.CreateInstanceRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import com.baiyi.opscloud.aliyun.ecs.base.AliyunInstanceTypeVO;
import com.baiyi.opscloud.domain.BusinessWrapper;

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
     *
     * @param regionId
     * @return
     */
    Map<String, Set<String>> getInstanceTypeZoneMap(String regionId);

    List<DescribeVSwitchesResponse.VSwitch> getVSwitchList(String regionId, String vpcId);

    List<DescribeInstancesResponse.Instance> getInstanceList(String regionId, List<String> instanceIds);

    DescribeInstancesResponse.Instance getStoppedInstance(String regionId, String hostname) throws Exception;

    String allocateInstancePublicIp(String regionId, String instanceId) throws Exception;

    boolean startInstance(String regionId, String instanceId) throws Exception;

    BusinessWrapper<String> getCreateInstanceResponse(String regionId, CreateInstanceRequest createInstanceRequest);
}
