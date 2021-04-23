package com.baiyi.opscloud.cloud.mq.impl;

import com.aliyuncs.ons.model.v20190214.OnsInstanceBaseInfoResponse;
import com.aliyuncs.ons.model.v20190214.OnsInstanceInServiceListResponse;
import com.baiyi.opscloud.aliyun.ons.AliyunONSInstance;
import com.baiyi.opscloud.cloud.mq.AliyunONSInstanceCenter;
import com.baiyi.opscloud.cloud.mq.builder.AliyunONSInstanceBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsInstance;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;
import com.baiyi.opscloud.service.aliyun.ons.OcAliyunOnsInstanceService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/5 1:54 下午
 * @Since 1.0
 */

@Slf4j
@Component("AliyunONSInstanceCenter")
public class AliyunONSInstanceCenterImpl implements AliyunONSInstanceCenter {

    @Resource
    private AliyunONSInstance aliyunONSInstance;

    @Resource
    private OcAliyunOnsInstanceService ocAliyunOnsInstanceService;

    @Override
    public Boolean syncONSInstance(String regionId) {
        HashMap<String, OcAliyunOnsInstance> map = getInstanceMap(regionId);
        List<OnsInstanceInServiceListResponse.InstanceVO> instanceList = aliyunONSInstance.queryInstanceList(regionId);
        instanceList.forEach(instance -> {
            saveInstance(instance, regionId);
            map.remove(instance.getInstanceId());
        });
        delInstanceByMap(map);
        return true;
    }

    private HashMap<String, OcAliyunOnsInstance> getInstanceMap(String regionId) {
        List<OcAliyunOnsInstance> instanceList = ocAliyunOnsInstanceService.queryOcAliyunOnsInstanceByRegionId(regionId);
        HashMap<String, OcAliyunOnsInstance> map = Maps.newHashMap();
        instanceList.forEach(instance -> map.put(instance.getInstanceId(), instance));
        return map;
    }

    private void saveInstance(OnsInstanceInServiceListResponse.InstanceVO instance, String regionId) {
        OcAliyunOnsInstance ocAliyunOnsInstance = ocAliyunOnsInstanceService.queryOcAliyunOnsInstanceByInstanceId(instance.getInstanceId());
        OcAliyunOnsInstance newOcAliyunOnsInstance = AliyunONSInstanceBuilder.build(instance, regionId);
        if (ocAliyunOnsInstance == null) {
            try {
                ocAliyunOnsInstanceService.addOcAliyunOnsInstance(newOcAliyunOnsInstance);
            } catch (Exception e) {
                log.error("新增阿里云ons实例信息失败，实例id:{}", instance.getInstanceId(), e);
            }
        }
    }

    private void delInstanceByMap(HashMap<String, OcAliyunOnsInstance> map) {
        map.forEach((key, value) -> ocAliyunOnsInstanceService.deleteOcAliyunOnsInstanceById(value.getId()));
    }

    @Override
    public Boolean updateONSInstanceDetail(AliyunONSParam.QueryInstanceDetail param) {
        OcAliyunOnsInstance ocAliyunOnsInstance = ocAliyunOnsInstanceService.queryOcAliyunOnsInstanceByInstanceId(param.getInstanceId());
        OnsInstanceBaseInfoResponse.InstanceBaseInfo instanceBaseInfo = aliyunONSInstance.queryInstanceDetail(param);
        ocAliyunOnsInstance = decoratorONSInstance(ocAliyunOnsInstance, instanceBaseInfo);
        try {
            ocAliyunOnsInstanceService.updateOcAliyunOnsInstance(ocAliyunOnsInstance);
            return true;
        } catch (Exception e) {
            log.error("更新阿里云ons实例信息失败，实例id:{}", ocAliyunOnsInstance.getInstanceId(), e);
        }
        return false;
    }

    private OcAliyunOnsInstance decoratorONSInstance(OcAliyunOnsInstance ocAliyunOnsInstance, OnsInstanceBaseInfoResponse.InstanceBaseInfo instanceBaseInfo) {
        ocAliyunOnsInstance.setMaxTps(instanceBaseInfo.getMaxTps());
        ocAliyunOnsInstance.setTopicCapacity(instanceBaseInfo.getTopicCapacity());
        ocAliyunOnsInstance.setRemark(instanceBaseInfo.getRemark());
        ocAliyunOnsInstance.setInstanceName(instanceBaseInfo.getInstanceName());
        ocAliyunOnsInstance.setIndependentNaming(instanceBaseInfo.getIndependentNaming());
        ocAliyunOnsInstance.setInstanceStatus(instanceBaseInfo.getInstanceStatus());
        ocAliyunOnsInstance.setInstanceType(instanceBaseInfo.getInstanceType());
        ocAliyunOnsInstance.setReleaseTime(instanceBaseInfo.getReleaseTime());
        if (instanceBaseInfo.getEndpoints() != null) {
            ocAliyunOnsInstance.setTcpEndpoint(instanceBaseInfo.getEndpoints().getTcpEndpoint());
            ocAliyunOnsInstance.setHttpInternalEndpoint(instanceBaseInfo.getEndpoints().getHttpInternalEndpoint());
            ocAliyunOnsInstance.setHttpInternetEndpoint(instanceBaseInfo.getEndpoints().getHttpInternetEndpoint());
            ocAliyunOnsInstance.setHttpInternetSecureEndpoint(instanceBaseInfo.getEndpoints().getHttpInternetSecureEndpoint());
        }
        return ocAliyunOnsInstance;
    }
}
