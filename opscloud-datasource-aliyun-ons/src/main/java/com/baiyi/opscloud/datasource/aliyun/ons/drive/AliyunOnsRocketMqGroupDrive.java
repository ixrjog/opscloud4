package com.baiyi.opscloud.datasource.aliyun.ons.drive;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ons.model.v20190214.*;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsRocketMqGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
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

    public static final String QUERY_ALL = Strings.EMPTY;

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

    public OnsRocketMqGroup.Group getGroup(String regionId, AliyunConfig.Aliyun aliyun, String instanceId, String groupId) throws ClientException {
        List<OnsRocketMqGroup.Group> list = listGroup(regionId, aliyun, instanceId, groupId, QUERY_ALL);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
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

    /**
     * https://help.aliyun.com/document_detail/29616.html
     *
     * @param regionId
     * @param aliyun
     * @param group
     * @throws ClientException
     */
    public void createGroup(String regionId, AliyunConfig.Aliyun aliyun, OnsRocketMqGroup.Group group) throws ClientException {
        OnsGroupCreateRequest request = new OnsGroupCreateRequest();
        request.setInstanceId(group.getInstanceId());
        request.setGroupId(group.getGroupId());
        request.setRemark(group.getRemark());
        request.setGroupType(group.getGroupType());
        OnsGroupCreateResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        log.info("创建阿里云ONS-Topic: requestId = {}, group = {}", response.getRequestId(), group);
    }

}
