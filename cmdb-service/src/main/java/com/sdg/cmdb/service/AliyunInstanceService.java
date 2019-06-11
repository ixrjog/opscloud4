package com.sdg.cmdb.service;

import com.aliyuncs.ecs.model.v20140526.DescribeDisksResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceStatusResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;

import java.util.List;

public interface AliyunInstanceService {

    /**
     * 查询ECS实例 运行状态 "Status":"Running", "Status":"Stopped"
     * @param regionId
     * @return
     */
    List<DescribeInstanceStatusResponse.InstanceStatus> getInstanceStatusList(String regionId);

    List<DescribeInstancesResponse.Instance> getInstanceList(String regionId);

    DescribeInstancesResponse.Instance getInstance(String regionId, String instanceId);

    /**
     * 按IP查询 Instance
     * @param regionId
     * @param ip
     * @return
     */
    DescribeInstancesResponse.Instance getInstanceByIp(String regionId, String ip);

    List<DescribeDisksResponse.Disk> queryDisks(String regionId, String instanceId);

    /**
     * 修改Instance名称
     * @param regionId
     * @param instanceId
     * @param instanceName
     * @return
     */
    boolean modifyInstanceName(String regionId, String instanceId, String instanceName);

    int getTotalCount(String RegionId);
}
