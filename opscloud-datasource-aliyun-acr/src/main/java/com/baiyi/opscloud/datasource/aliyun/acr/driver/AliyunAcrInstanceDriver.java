package com.baiyi.opscloud.datasource.aliyun.acr.driver;

import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

//    public List<ListInstanceResponse.InstancesItem> listInstance() {
//        try {
//            IAcsClient client = aliyunCore.getMasterClient();
//            ListInstanceRequest request = new ListInstanceRequest();
//            ListInstanceResponse response = client.getAcsResponse(request);
//            return response.getInstances();
//        } catch (ClientException e) {
//            return Collections.emptyList();
//        }
//    }
//
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
