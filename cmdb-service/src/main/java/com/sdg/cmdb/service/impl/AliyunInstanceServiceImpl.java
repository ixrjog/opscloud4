package com.sdg.cmdb.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.sdg.cmdb.service.AliyunInstanceService;
import com.sdg.cmdb.service.AliyunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class AliyunInstanceServiceImpl implements AliyunInstanceService {

    @Autowired
    private AliyunService aliyunService;

    public static final String regionIdCnHangzhou = "cn-hangzhou"; // 华东1


    @Override
    public List<DescribeInstanceStatusResponse.InstanceStatus> getInstanceStatusList(String regionId) {
        if (regionId == null) regionId = regionIdCnHangzhou;
        List<DescribeInstanceStatusResponse.InstanceStatus> instanceStatusList = new ArrayList<>();
        int pageSize = 50;
        DescribeInstanceStatusRequest describe = new DescribeInstanceStatusRequest();
        describe.setPageSize(pageSize);
        if (regionId == null) regionId = regionIdCnHangzhou;
        DescribeInstanceStatusResponse response = sampleDescribeInstanceStatusResponse(regionId, describe);
        instanceStatusList.addAll(response.getInstanceStatuses());
        int totalCount = response.getTotalCount();
        // 循环次数
        int cnt = (totalCount + pageSize - 1) / pageSize;
        for (int i = 1; i < cnt; i++) {
            describe.setPageNumber(i + 1);
            response = sampleDescribeInstanceStatusResponse(regionId, describe);
            instanceStatusList.addAll(response.getInstanceStatuses());
        }
        return instanceStatusList;
    }

    /**
     * 查询ecs状态
     *
     * @param regionId
     * @param describe
     * @return
     */
    private DescribeInstanceStatusResponse sampleDescribeInstanceStatusResponse(String regionId, DescribeInstanceStatusRequest describe) {
        if (regionId == null) regionId = regionIdCnHangzhou;
        IAcsClient client = acqIAcsClient(regionId);
        try {
            DescribeInstanceStatusResponse response
                    = client.getAcsResponse(describe);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询当前区域所有服务器属性
     *
     * @param regionId
     * @return
     */
    @Override
    public List<DescribeInstancesResponse.Instance> getInstanceList(String regionId) {
        if (regionId == null) regionId = regionIdCnHangzhou;
        List<DescribeInstancesResponse.Instance> instanceList = new ArrayList<>();
        int pageSize = 50;
        DescribeInstancesRequest describe = new DescribeInstancesRequest();
        describe.setPageSize(pageSize);
        DescribeInstancesResponse response = sampleDescribeInstancesResponse(regionId, describe);
        instanceList.addAll(response.getInstances());
        //获取总数
        int totalCount = response.getTotalCount();
        // 循环次数
        int cnt = (totalCount + pageSize - 1) / pageSize;
        for (int i = 1; i < cnt; i++) {
            describe.setPageNumber(i + 1);
            response = sampleDescribeInstancesResponse(regionId, describe);
            instanceList.addAll(response.getInstances());
        }
        return instanceList;
    }

    private DescribeInstancesResponse sampleDescribeInstancesResponse(String regionId, DescribeInstancesRequest describe) {
        if (regionId == null) regionId = regionIdCnHangzhou;
        IAcsClient client;
        client = acqIAcsClient(regionId);
        try {
            DescribeInstancesResponse response = client.getAcsResponse(describe);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DescribeInstancesResponse.Instance getInstance(String regionId, String instanceId) {
        if (regionId == null) regionId = regionIdCnHangzhou;
        DescribeInstancesRequest describe = new DescribeInstancesRequest();
        if (regionId == null) regionId = regionIdCnHangzhou;
        describe.setRegionId(regionId);
        JSONArray instanceIds = new JSONArray();
        instanceIds.add(instanceId);
        describe.setInstanceIds(instanceIds.toString());
        try {
            DescribeInstancesResponse response = sampleDescribeInstancesResponse(regionId, describe);
            DescribeInstancesResponse.Instance ecs = response.getInstances().get(0);
            return ecs;
        } catch (Exception e) {
            return new DescribeInstancesResponse.Instance();
        }
    }

    @Override
    public DescribeInstancesResponse.Instance getInstanceByIp(String regionId, String ip) {
        DescribeInstancesRequest describe = new DescribeInstancesRequest();
        if (regionId == null) regionId = regionIdCnHangzhou;
        describe.setRegionId(regionId);
        JSONArray ips = new JSONArray();
        ips.add(ip);
        describe.setInnerIpAddresses(ips.toString());
        DescribeInstancesResponse response = sampleDescribeInstancesResponse(regionId, describe);
        return response.getInstances().get(0);
    }

    @Override
    public List<DescribeDisksResponse.Disk> queryDisks(String regionId, String instanceId) {
        if (regionId == null) regionId = regionIdCnHangzhou;
        DescribeDisksRequest request = new DescribeDisksRequest();
        request.setInstanceId(instanceId);
        DescribeDisksResponse response = sampleDescribeDisksResponse(regionId, request);
        if (response == null || response.getRequestId().isEmpty()) return new ArrayList<DescribeDisksResponse.Disk>();
        return response.getDisks();
    }


    /**
     * 查询磁盘
     *
     * @param regionId
     * @param request
     * @return
     */
    private DescribeDisksResponse sampleDescribeDisksResponse(String regionId, DescribeDisksRequest request) {
        IAcsClient client;
        client = acqIAcsClient(regionId);
        try {
            DescribeDisksResponse response = client.getAcsResponse(request);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean modifyInstanceName(String regionId, String instanceId, String instanceName) {
        if (StringUtils.isEmpty(instanceId) || StringUtils.isEmpty(instanceName))
            return false;
        DescribeInstancesResponse.Instance instance = getInstance(regionId, instanceId);
        if (instance == null) return false;
        ModifyInstanceAttributeRequest describe = new ModifyInstanceAttributeRequest();

        describe.setInstanceId(instanceId);
        describe.setInstanceName(instanceName);
        ModifyInstanceAttributeResponse response = sampleModifyInstanceAttributeResponse(regionId, describe);
        if (response == null || response.getRequestId().isEmpty()) return false;
        return true;
    }

    /**
     * 修改Instance属性
     *
     * @param regionId
     * @param describe
     * @return
     */
    private ModifyInstanceAttributeResponse sampleModifyInstanceAttributeResponse(String regionId, ModifyInstanceAttributeRequest describe) {
        if (regionId == null) regionId = regionIdCnHangzhou;
        IAcsClient client = acqIAcsClient(regionId);
        try {
            ModifyInstanceAttributeResponse response
                    = client.getAcsResponse(describe);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 查询当前区域服务器总数
     *
     * @param RegionId
     * @return
     */
    @Override
    public int getTotalCount(String RegionId) {
        DescribeInstancesRequest describe = new DescribeInstancesRequest();
        DescribeInstancesResponse response = sampleDescribeInstancesResponse(RegionId, describe);
        return response.getTotalCount();
    }

    private IAcsClient acqIAcsClient(String regionId) {
        return aliyunService.acqIAcsClient(regionId);
    }

}
