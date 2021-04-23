package com.baiyi.opscloud.aliyun.ecs.handler;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.aliyun.ecs.base.BaseAliyunECS;
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
@Component("AliyunECSHandler")
public class AliyunECSHandler extends BaseAliyunECS {

    @Resource
    private AliyunInstanceHandler aliyunInstanceHandler;

    public List<DescribeInstanceAutoRenewAttributeResponse.InstanceRenewAttribute> getInstanceRenewAttribute(String regionId, DescribeInstanceAutoRenewAttributeRequest describe) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            return client.getAcsResponse(describe).getInstanceRenewAttributes();
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    public DescribeDisksResponse getDisksResponse(String regionId, DescribeDisksRequest request) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            return client.getAcsResponse(request);
        } catch (Exception e) {
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
            DescribeInstancesResponse response = aliyunInstanceHandler.getInstancesResponse(regionId, describe);
            return response.getInstances().get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public List<DescribeInstancesResponse.Instance> getInstanceList(String regionId) {
        List<DescribeInstancesResponse.Instance> instanceList = Lists.newArrayList();
        try {
            DescribeInstancesRequest describe = new DescribeInstancesRequest();
            describe.setPageSize(QUERY_INSTANCE_PAGE_SIZE);
            int size = QUERY_INSTANCE_PAGE_SIZE;
            int pageNumber = 1;
            // 循环取值
            while (QUERY_INSTANCE_PAGE_SIZE <= size) {
                describe.setPageNumber(pageNumber);
                DescribeInstancesResponse response = aliyunInstanceHandler.getInstancesResponse(regionId, describe);
                instanceList.addAll(response.getInstances());
                size = response.getInstances() != null ? response.getInstances().size() : 0;
                pageNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instanceList;
    }

    public BusinessWrapper<Boolean> start(String regionId, String instanceId) {
        try {
            StartInstanceRequest describe = new StartInstanceRequest();
            describe.setInstanceId(instanceId);
            StartInstanceResponse response = startInstanceResponse(regionId, describe);
            if (response != null && !StringUtils.isEmpty(response.getRequestId()))
                return BusinessWrapper.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BusinessWrapper<>(ErrorEnum.CLOUD_SERVER_POWER_MGMT_FAILED);
    }

    public BusinessWrapper<Boolean> stop(String regionId, String instanceId) {
        try {
            StopInstanceRequest describe = new StopInstanceRequest();
            describe.setInstanceId(instanceId);
            StopInstanceResponse response = stopInstanceResponse(regionId, describe);
            if (response != null && !StringUtils.isEmpty(response.getRequestId()))
                return BusinessWrapper.SUCCESS;
        } catch (Exception ignored) {
        }
        return new BusinessWrapper<>(ErrorEnum.CLOUD_SERVER_POWER_MGMT_FAILED);
    }

    private StopInstanceResponse stopInstanceResponse(String regionId, StopInstanceRequest describe) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            return client.getAcsResponse(describe);
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    private StartInstanceResponse startInstanceResponse(String regionId, StartInstanceRequest describe) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            return client.getAcsResponse(describe);
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean delete(String regionId, String instanceId) {
        try {
            DeleteInstanceRequest request = new DeleteInstanceRequest();
            request.setInstanceId(instanceId);
            DeleteInstanceResponse response = deleteInstanceResponse(regionId, request);
            if (response != null && !StringUtils.isEmpty(response.getRequestId()))
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private DeleteInstanceResponse deleteInstanceResponse(String regionId, DeleteInstanceRequest request) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void modifyInstanceChargeType(String regionId, String instanceId, String chargeType) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            ModifyInstanceChargeTypeRequest request = new ModifyInstanceChargeTypeRequest();
            List<String> s = Lists.newArrayList(instanceId);
            request.setInstanceIds(JSON.toJSONString(s));
            request.setInstanceChargeType(chargeType);
            // 将按量付费实例转换为包年包月实例
            if ("PrePaid".equals(chargeType)) {
                request.setPeriodUnit("Month");
                request.setPeriod(3);
                request.setIncludeDataDisks(true);
                request.setAutoPay(true);
            }
            // 将包年包月实例转换为按量付费实例
//            if ("PostPaid".equals(chargeType)) {
//            }
            ModifyInstanceChargeTypeResponse response = client.getAcsResponse(request);
            // return !Strings.isNullOrEmpty(response.getOrderId());
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }


    public Boolean modifyInstanceName(String regionId, String instanceId, String instanceName) {
        if (StringUtils.isEmpty(instanceId) || StringUtils.isEmpty(instanceName))
            return false;
        DescribeInstancesResponse.Instance instance = getInstance(regionId, instanceId);
        if (instance == null) return false;
        ModifyInstanceAttributeRequest describe = new ModifyInstanceAttributeRequest();
        describe.setInstanceId(instanceId);
        describe.setInstanceName(instanceName);
        /**
         * 修改 hostname
         * 经测试，仅在ECS实例关机后方可生效
         */
        describe.setHostName(instanceName);
        try {
            ModifyInstanceAttributeResponse response = modifyInstanceAttributeResponse(regionId, describe);
            return response != null && !response.getRequestId().isEmpty();
        } catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 修改Instance属性
     *
     * @param regionId
     * @param describe
     * @return
     */
    private ModifyInstanceAttributeResponse modifyInstanceAttributeResponse(String regionId, ModifyInstanceAttributeRequest describe) throws ClientException {
        IAcsClient client = acqAcsClient(regionId);
        return client.getAcsResponse(describe);
    }


}
