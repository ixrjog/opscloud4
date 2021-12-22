package com.baiyi.opscloud.datasource.aliyun.ons.drive;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ons.model.v20190214.OnsGroupListRequest;
import com.aliyuncs.ons.model.v20190214.OnsGroupListResponse;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import entity.OnsRocketMqGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<OnsRocketMqGroup.Group> listGroup(String regionId, AliyunConfig.Aliyun aliyun, String instanceId) throws ClientException {
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
    public List<OnsRocketMqGroup.Group> listGroup(String regionId, AliyunConfig.Aliyun aliyun, String instanceId, String groupId, String groupType) throws ClientException {
        OnsGroupListRequest request = new OnsGroupListRequest();
        request.setInstanceId(instanceId);
        if (StringUtils.isBlank(groupId)) {
            request.setGroupId(groupId);
        }
        if (StringUtils.isBlank(groupType)) {
            request.setGroupType(groupType);
        }
        OnsGroupListResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        if (response == null || CollectionUtils.isEmpty(response.getData()))
            return Collections.emptyList();
        return response.getData().stream().map(e -> {
            OnsRocketMqGroup.Group g = BeanCopierUtil.copyProperties(e, OnsRocketMqGroup.Group.class);
            g.setRegionId(regionId);
            return g;
        }).collect(Collectors.toList());
    }
}
