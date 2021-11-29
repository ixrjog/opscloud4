package com.baiyi.opscloud.datasource.aliyun.ons.rocketmq.drive;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ons.model.v20190214.OnsGroupListRequest;
import com.aliyuncs.ons.model.v20190214.OnsGroupListResponse;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/30 4:20 下午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunOnsRocketMqGroupDrive {

    private final AliyunClient aliyunClient;

    public static final String QUERY_ALL = null;

    /**
     * ONS Group
     *
     * @param regionId
     * @param aliyun
     * @param instanceId 必选参数
     * @return
     */
    public List<OnsGroupListResponse.SubscribeInfoDo> listGroup(String regionId, AliyunConfig.Aliyun aliyun, String instanceId) {
        return listGroup(regionId, aliyun, instanceId, QUERY_ALL, QUERY_ALL);
    }

    /**
     * ONS Group
     *
     * @param regionId
     * @param aliyun
     * @param instanceId 必选参数
     * @param groupId
     * @param groupType
     * @return
     */
    public List<OnsGroupListResponse.SubscribeInfoDo> listGroup(String regionId, AliyunConfig.Aliyun aliyun, String instanceId, String groupId, String groupType) {
        OnsGroupListRequest request = new OnsGroupListRequest();
        request.setInstanceId(instanceId);
        if (StringUtils.isBlank(groupId)) {
            request.setGroupId(groupId);
        }
        if (StringUtils.isBlank(groupType)) {
            request.setGroupType(groupType);
        }
        try {
            OnsGroupListResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            return response == null ? Collections.emptyList() : response.getData();
        } catch (ClientException e) {
            log.error("查询ONSGroup列表失败", e);
            return Collections.emptyList();
        }
    }
}
