package com.baiyi.opscloud.aliyun.ecs.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.common.util.JSONUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/14 10:09 上午
 * @Version 1.0
 */
@Component
public class AliyunECSHandler {

    @Resource
    private AliyunCore aliyunCore;

    public static final int QUERY_PAGE_SIZE = 50;

    public List<DescribeInstanceAutoRenewAttributeResponse.InstanceRenewAttribute> getInstanceRenewAttribute(String regionId, DescribeInstanceAutoRenewAttributeRequest describe) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            DescribeInstanceAutoRenewAttributeResponse response
                    = client.getAcsResponse(describe);
            return response.getInstanceRenewAttributes();
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    public DescribeDisksResponse getDisksResponse(String regionId, DescribeDisksRequest request) {
        IAcsClient client;
        client = acqAcsClient(regionId);
        try {
            DescribeDisksResponse response = client.getAcsResponse(request);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public DescribeInstancesResponse getInstancesResponse(String regionId, DescribeInstancesRequest describe) {
        IAcsClient client;
        client = acqAcsClient(regionId);
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

    public DescribeInstancesResponse.Instance getInstance(String regionId, String instanceId) {
        DescribeInstancesRequest describe = new DescribeInstancesRequest();
        Collection<String> instanceIds = Lists.newArrayList();
        instanceIds.add(instanceId);
        describe.setInstanceIds(JSONUtils.writeValueAsString(instanceIds));
        try {
            DescribeInstancesResponse response = getInstancesResponse(regionId, describe);
            return response.getInstances().get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public List<DescribeInstancesResponse.Instance> getInstanceList(String regionId) {
        List<DescribeInstancesResponse.Instance> instanceList = Lists.newArrayList();
        DescribeInstancesRequest describe = new DescribeInstancesRequest();
        describe.setPageSize(QUERY_PAGE_SIZE);
        DescribeInstancesResponse response = getInstancesResponse(regionId, describe);
        instanceList.addAll(response.getInstances());
        //cacheInstanceRenewAttribute(regionId, response);
        // 获取总数
        int totalCount = response.getTotalCount();
        // 循环次数
        int cnt = (totalCount + QUERY_PAGE_SIZE - 1) / QUERY_PAGE_SIZE;
        for (int i = 1; i < cnt; i++) {
            describe.setPageNumber(i + 1);
            response = getInstancesResponse(regionId, describe);
            instanceList.addAll(response.getInstances());
            //cacheInstanceRenewAttribute(regionId, response);
        }
        return instanceList;
    }

    public BusinessWrapper<Boolean> start(String regionId,String instanceId) {
        try {
            StartInstanceRequest describe = new StartInstanceRequest();
            describe.setInstanceId(instanceId);
            StartInstanceResponse response = startInstanceResponse(regionId, describe);
            if (response != null && !StringUtils.isEmpty(response.getRequestId()))
                return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
        }
        return new BusinessWrapper<Boolean>(ErrorEnum.CLOUD_SERVER_POWER_MGMT_FAILED);
    }


    public BusinessWrapper<Boolean> stop(String regionId,String instanceId) {
        try {
            StopInstanceRequest describe = new StopInstanceRequest();
            describe.setInstanceId(instanceId);
            StopInstanceResponse response = stopInstanceResponse(regionId, describe);
            if (response != null && !StringUtils.isEmpty(response.getRequestId()))
                return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
        }
        return new BusinessWrapper<Boolean>(ErrorEnum.CLOUD_SERVER_POWER_MGMT_FAILED);
    }


    private StopInstanceResponse stopInstanceResponse(String regionId, StopInstanceRequest describe) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            StopInstanceResponse response = client.getAcsResponse(describe);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    private StartInstanceResponse startInstanceResponse(String regionId, StartInstanceRequest describe) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            StartInstanceResponse response = client.getAcsResponse(describe);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }


    private IAcsClient acqAcsClient(String regionId) {
        return aliyunCore.getAcsClient(regionId);
    }


}
