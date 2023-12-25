package com.baiyi.opscloud.datasource.aliyun.ons.driver;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ons.model.v20190214.OnsInstanceBaseInfoRequest;
import com.aliyuncs.ons.model.v20190214.OnsInstanceBaseInfoResponse;
import com.aliyuncs.ons.model.v20190214.OnsInstanceInServiceListRequest;
import com.aliyuncs.ons.model.v20190214.OnsInstanceInServiceListResponse;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import com.google.common.collect.Lists;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsInstance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/30 2:08 下午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunOnsRocketMqInstanceDriver {

    private final AliyunClient aliyunClient;

    public List<OnsInstance.InstanceBaseInfo> listInstance(String regionId, AliyunConfig.Aliyun aliyun) throws ClientException {
        OnsInstanceInServiceListRequest request = new OnsInstanceInServiceListRequest();
        OnsInstanceInServiceListResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        if (response == null || CollectionUtils.isEmpty(response.getData())) {
            return Collections.emptyList();
        }
        List<OnsInstance.InstanceBaseInfo> instanceBaseInfos = Lists.newArrayList();
        for (OnsInstanceInServiceListResponse.InstanceVO i : response.getData()) {
            instanceBaseInfos.add(getInstanceInfo(regionId, aliyun, i.getInstanceId()));
        }
        return  instanceBaseInfos;
    }

    public OnsInstance.InstanceBaseInfo getInstanceInfo(String regionId, AliyunConfig.Aliyun aliyun, String instanceId) throws ClientException {
        OnsInstanceBaseInfoRequest request = new OnsInstanceBaseInfoRequest();
        request.setInstanceId(instanceId);
        OnsInstanceBaseInfoResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        OnsInstance.InstanceBaseInfo instanceBaseInfo = BeanCopierUtil.copyProperties(response.getInstanceBaseInfo(), OnsInstance.InstanceBaseInfo.class);
        instanceBaseInfo.setEndpoints(BeanCopierUtil.copyProperties(response.getInstanceBaseInfo().getEndpoints(), OnsInstance.Endpoints.class));
        instanceBaseInfo.setRegionId(regionId);
        return instanceBaseInfo;
    }

}