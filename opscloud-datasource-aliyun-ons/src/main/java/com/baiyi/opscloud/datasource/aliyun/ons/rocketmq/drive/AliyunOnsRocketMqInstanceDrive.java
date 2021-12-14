package com.baiyi.opscloud.datasource.aliyun.ons.rocketmq.drive;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ons.model.v20190214.OnsInstanceInServiceListRequest;
import com.aliyuncs.ons.model.v20190214.OnsInstanceInServiceListResponse;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import entity.OnsInstance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/9/30 2:08 下午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunOnsRocketMqInstanceDrive {

    private final AliyunClient aliyunClient;

    public List<OnsInstance.Instance> listInstance(String regionId, AliyunConfig.Aliyun aliyun) throws ClientException {
        OnsInstanceInServiceListRequest request = new OnsInstanceInServiceListRequest();
        OnsInstanceInServiceListResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        if (response == null || CollectionUtils.isEmpty(response.getData()))
            return Collections.emptyList();
        return response.getData().stream().map(i -> {
            OnsInstance.Instance instance = BeanCopierUtil.copyProperties(i, OnsInstance.Instance.class);
            instance.setRegionId(regionId);
            return instance;

        }).collect(Collectors.toList());
    }

//    public OnsInstanceBaseInfoResponse.InstanceBaseInfo getInstanceInfo(String regionId, AliyunConfig.Aliyun aliyun, String instanceId) {
//        OnsInstanceBaseInfoRequest request = new OnsInstanceBaseInfoRequest();
//        request.setInstanceId(instanceId);
//        try {
//            OnsInstanceBaseInfoResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
//            return response.getInstanceBaseInfo();
//        } catch (ClientException e) {
//            log.error("查询ONS实例详情失败", e);
//            return null;
//        }
//    }

}
