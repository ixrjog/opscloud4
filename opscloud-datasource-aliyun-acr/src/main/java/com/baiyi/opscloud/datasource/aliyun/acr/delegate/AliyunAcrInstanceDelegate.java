package com.baiyi.opscloud.datasource.aliyun.acr.delegate;

import com.aliyuncs.cr.model.v20181201.GetInstanceEndpointResponse;
import com.aliyuncs.cr.model.v20181201.GetInstanceResponse;
import com.aliyuncs.cr.model.v20181201.ListInstanceResponse;
import com.aliyuncs.cr.model.v20181201.ListNamespaceResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.acr.driver.AliyunAcrInstanceDriver;
import com.baiyi.opscloud.datasource.aliyun.acr.entity.AliyunAcr;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/8/15 14:32
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AliyunAcrInstanceDelegate {

    private final AliyunAcrInstanceDriver aliyunAcrInstanceDriver;

    public AliyunAcr.Instance getInstance(String regionId, AliyunConfig.Aliyun aliyun, String instanceId) throws ClientException {
        GetInstanceResponse getInstanceResponse = aliyunAcrInstanceDriver.getInstance(regionId, aliyun, instanceId);
        return BeanCopierUtil.copyProperties(getInstanceResponse, AliyunAcr.Instance.class);
    }

    public List<AliyunAcr.Instance> listInstance(String regionId, AliyunConfig.Aliyun aliyun) throws ClientException {
        List<ListInstanceResponse.InstancesItem> instancesItems = aliyunAcrInstanceDriver.listInstance(regionId, aliyun);
        List<AliyunAcr.Instance> instances = Lists.newArrayList();
        for (ListInstanceResponse.InstancesItem instancesItem : instancesItems) {
            instances.add(buildInstance(regionId, aliyun, instancesItem));
        }
        return instances;
    }

    public List<AliyunAcr.Namespace> listNamespace(String regionId, AliyunConfig.Aliyun aliyun, String instanceId) throws ClientException {
        List<ListNamespaceResponse.NamespacesItem> namespacesItems = aliyunAcrInstanceDriver.listNamespace(regionId, aliyun, instanceId);
        return namespacesItems.stream().map(e -> {
            AliyunAcr.Namespace namespace = BeanCopierUtil.copyProperties(e, AliyunAcr.Namespace.class);
            namespace.setRegionId(regionId);
            return namespace;
        }).collect(Collectors.toList());
    }

    private AliyunAcr.Instance buildInstance(String regionId, AliyunConfig.Aliyun aliyun, ListInstanceResponse.InstancesItem instancesItem) throws ClientException {
        AliyunAcr.Instance instance = BeanCopierUtil.copyProperties(instancesItem, AliyunAcr.Instance.class);
        Optional<List<AliyunConfig.Acr.Instance>> optionalAcrInstances = Optional.ofNullable(aliyun)
                .map(AliyunConfig.Aliyun::getArc)
                .map(AliyunConfig.Acr::getInstances);
        if (optionalAcrInstances.isPresent()) {
            // 从数据源配置文件中获取 Endpoint
            Optional<AliyunConfig.Acr.Instance> optionalInstance = optionalAcrInstances.get().stream().filter(e -> instance.getInstanceId().equals(e.getId())).findFirst();
            if (optionalInstance.isPresent()) {
                AliyunAcr.Endpoint endpoint = AliyunAcr.Endpoint.builder()
                        .domain(optionalInstance.get().getDomain())
                        .build();
                instance.setEndpoint(endpoint);
                return instance;
            }
        }
        // 从API获取
        List<GetInstanceEndpointResponse.Endpoints> endpoints = aliyunAcrInstanceDriver.getInstanceEndpoint(regionId, aliyun, instance.getInstanceId());
        Optional<GetInstanceEndpointResponse.Endpoints> optionalEndpoint = endpoints.stream().filter(e -> "USER".equals(e.getType())).findFirst();
        GetInstanceEndpointResponse.Endpoints ep = optionalEndpoint.orElseGet(endpoints::getFirst);
        AliyunAcr.Endpoint endpoint = AliyunAcr.Endpoint.builder()
                .domain(ep.getDomain())
                .build();
        instance.setEndpoint(endpoint);
        return instance;
    }

}