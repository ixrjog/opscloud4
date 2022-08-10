package com.baiyi.opscloud.datasource.aliyun.acr.driver;

import com.aliyuncs.cr.model.v20181201.ListInstanceRequest;
import com.aliyuncs.cr.model.v20181201.ListInstanceResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.acr.entity.AliyunAcr;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/7/12 13:33
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunAcrInstanceDriver {

    private final AliyunClient aliyunClient;

    public List<ListInstanceResponse.InstancesItem> listInstance(String regionId, AliyunConfig.Aliyun aliyun) {
        try {
            ListInstanceRequest request = new ListInstanceRequest();
            ListInstanceResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            return response.getInstances();
        } catch (ClientException e) {
            return Collections.emptyList();
        }
    }

    private List<AliyunAcr.Instance> toInstances(List<ListInstanceResponse.InstancesItem> instancesItems) {
        return BeanCopierUtil.copyListProperties(instancesItems, AliyunAcr.Instance.class);
    }

//    public ListInstanceEndpointResponse listInstanceEndpoint(String regionId, String instanceId) {
//        try {
//            IAcsClient client = aliyunCore.getMasterClient();
//            ListInstanceEndpointRequest request = new ListInstanceEndpointRequest();
//            request.setRegionId(regionId);
//            request.setInstanceId(instanceId);
//            ListInstanceEndpointResponse response = client.getAcsResponse(request);
//            return response.getIsSuccess() ? response : null;
//        } catch (ClientException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

}
