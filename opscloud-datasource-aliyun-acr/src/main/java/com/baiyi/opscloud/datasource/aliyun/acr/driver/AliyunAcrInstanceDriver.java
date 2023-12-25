package com.baiyi.opscloud.datasource.aliyun.acr.driver;

import com.aliyuncs.cr.model.v20181201.*;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.acr.driver.base.BaseAliyunAcrDriver;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/7/12 13:33
 * @Version 1.0
 */
@Slf4j
@Component
public class AliyunAcrInstanceDriver extends BaseAliyunAcrDriver {

    /**
     * {
     * "defaultRepoType":"PRIVATE",
     * "namespaceStatus":"NORMAL",
     * "namespaceId":"crn-g0h399e0ayt6ax00",
     * "autoCreateRepo":true,
     * "instanceId":"cri-4v9b8l2gc3en0x34",
     * "namespaceName":"daily"
     * }
     *
     * @param regionId
     * @param aliyun
     * @param instanceId
     * @return
     * @throws ClientException
     */
    public List<ListNamespaceResponse.NamespacesItem> listNamespace(String regionId, AliyunConfig.Aliyun aliyun, String instanceId) throws ClientException {
        ListNamespaceRequest request = new ListNamespaceRequest();
        request.setSysRegionId(regionId);
        request.setInstanceId(instanceId);
        request.setNamespaceStatus(Query.NORMAL);
        request.setPageSize(Query.PAGE_SIZE);
        int total = 0;
        int pageNo = 1;
        List<ListNamespaceResponse.NamespacesItem> namespacesItems = Lists.newArrayList();
        while (total == 0 || total == namespacesItems.size()) {
            request.setPageNo(pageNo);
            ListNamespaceResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            List<ListNamespaceResponse.NamespacesItem> nowData = response.getNamespaces();
            if (CollectionUtils.isEmpty(nowData)) {
                break;
            } else {
                namespacesItems.addAll(nowData);
            }
            if (total == 0) {
                total = Integer.parseInt(response.getTotalCount());
            }
            pageNo++;
        }
        return namespacesItems;
    }

    /**
     * @param regionId
     * @param aliyun
     * @return
     * @throws ClientException
     */
    public List<ListInstanceResponse.InstancesItem> listInstance(String regionId, AliyunConfig.Aliyun aliyun) throws ClientException {
        ListInstanceRequest request = new ListInstanceRequest();
        request.setSysRegionId(regionId);
        request.setPageSize(Query.PAGE_SIZE);
        int total = 0;
        int pageNo = 1;
        List<ListInstanceResponse.InstancesItem> instancesItems = Lists.newArrayList();
        while (total == 0 || total == instancesItems.size()) {
            request.setPageNo(pageNo);
            ListInstanceResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            List<ListInstanceResponse.InstancesItem> nowData = response.getInstances();
            if (CollectionUtils.isEmpty(nowData)) {
                break;
            } else {
                instancesItems.addAll(nowData);
            }
            total = response.getTotalCount();
            pageNo++;
        }
        return instancesItems;
    }

    public GetInstanceResponse getInstance(String regionId, AliyunConfig.Aliyun aliyun, String instanceId) throws ClientException {
        GetInstanceRequest request = new GetInstanceRequest();
        request.setSysRegionId(regionId);
        request.setInstanceId(instanceId);
        return aliyunClient.getAcsResponse(regionId, aliyun, request);
    }

    public List<GetInstanceEndpointResponse.Endpoints> getInstanceEndpoint(String regionId, AliyunConfig.Aliyun aliyun, String instanceId) throws ClientException {
        GetInstanceEndpointRequest request = new GetInstanceEndpointRequest();
        request.setEndpointType("internet");
        request.setInstanceId(instanceId);
        request.setSysRegionId(regionId);
        GetInstanceEndpointResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        return response.getDomains();
    }

}