package com.baiyi.opscloud.aliyun.ecs.handler;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.baiyi.opscloud.aliyun.ecs.base.BaseAliyunECS;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.google.common.collect.Lists;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/19 6:08 下午
 * @Version 1.0
 */
@Component
public class AliyunInstanceHandler extends BaseAliyunECS {

    public List<DescribeZonesResponse.Zone> getZoneList(String regionId) {
        try {
            DescribeZonesRequest describe = new DescribeZonesRequest();
            describe.setSysRegionId(regionId);
            DescribeZonesResponse response = getDescribeZonesResponse(regionId, describe);
            return response.getZones();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    private DescribeZonesResponse getDescribeZonesResponse(String regionId, DescribeZonesRequest describe) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            return client.getAcsResponse(describe);
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询可用取实例类型
     *
     * @param regionId
     * @return
     */
    public List<DescribeInstanceTypesResponse.InstanceType> getInstanceTypeList(String regionId) {
        try {
            DescribeInstanceTypesRequest describe = new DescribeInstanceTypesRequest();
            describe.setSysRegionId(regionId);
            return getDescribeInstanceTypesResponse(regionId, describe).getInstanceTypes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private DescribeInstanceTypesResponse getDescribeInstanceTypesResponse(String regionId, DescribeInstanceTypesRequest describe) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            return client.getAcsResponse(describe);
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    public BusinessWrapper<String> getCreateInstanceResponse(String regionId, CreateInstanceRequest createInstanceRequest) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            CreateInstanceResponse response
                    = client.getAcsResponse(createInstanceRequest);
            return new BusinessWrapper(response.getInstanceId());
        } catch (ClientException e) {
            return new BusinessWrapper(35000, e.getErrCode());
        }
    }

    /**
     * 查询地区中所有已停止的实例（已停止的实例可能是新创建的）
     *
     * @param regionId
     * @return
     */
    public List<DescribeInstancesResponse.Instance> getStoppedInstance(String regionId) {
        DescribeInstancesRequest describe = new DescribeInstancesRequest();
        describe.setPageSize(QUERY_PAGE_SIZE);
        describe.setStatus("Stopped");
        try {
            DescribeInstancesResponse response = getInstancesResponse(regionId, describe);
            return response.getInstances();
        } catch (ClientException ce) {
            return null;
        }
    }

    public String allocateInstancePublicIp(String regionId, String instanceId) {
        try {
            AllocatePublicIpAddressRequest allocateIp = new AllocatePublicIpAddressRequest();
            allocateIp.setSysRegionId(regionId);
            allocateIp.setInstanceId(instanceId);

            AllocatePublicIpAddressResponse response = getAllocatePublicIpAddressResponse(regionId, allocateIp);
            if (response == null || response.getIpAddress().isEmpty()) return "";
            return response.getIpAddress();
        } catch (Exception e) {
            return "";
        }
    }

    private AllocatePublicIpAddressResponse getAllocatePublicIpAddressResponse(String regionId, AllocatePublicIpAddressRequest allocatePublicIpAddressRequest) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            return client.getAcsResponse(allocatePublicIpAddressRequest);
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            return null;
        }
    }

    @Retryable(value = ClientException.class, maxAttempts = 4, backoff = @Backoff(delay = 3000, multiplier = 1.5))
    public DescribeInstancesResponse getInstancesResponse(String regionId, DescribeInstancesRequest describe) throws ClientException {
        IAcsClient client = acqAcsClient(regionId);
        return client.getAcsResponse(describe);
    }

    /**
     * 启动实例
     *
     * @param regionId
     * @param instanceId
     * @return
     */
    public boolean startInstance(String regionId, String instanceId) {
        StartInstanceRequest describe = new StartInstanceRequest();
        describe.setInstanceId(instanceId);
        return startInstanceResponse(regionId, describe);
    }

    private boolean startInstanceResponse(String regionId, StartInstanceRequest describe) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            StartInstanceResponse response
                    = client.getAcsResponse(describe);
            if (!StringUtils.isEmpty(response.getRequestId()))
                return true;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            if (e.getErrCode().equals("IncorrectInstanceStatus")) {
            } else {
            }
        }
        return false;
    }

    public List<DescribeInstancesResponse.Instance> getInstanceList(String regionId, List<String> instanceIds) {
        DescribeInstancesRequest describe = new DescribeInstancesRequest();
        describe.setPageSize(instanceIds.size());
        describe.setSysRegionId(regionId);
        describe.setInstanceIds(JSON.toJSONString(instanceIds));
        try {
            DescribeInstancesResponse response = getInstancesResponse(regionId, describe);
            return response.getInstances();
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

}
