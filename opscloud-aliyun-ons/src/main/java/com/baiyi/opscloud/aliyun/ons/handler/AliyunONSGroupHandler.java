package com.baiyi.opscloud.aliyun.ons.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ons.model.v20190214.*;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/9 3:26 下午
 * @Since 1.0
 */

@Slf4j
@Component
public class AliyunONSGroupHandler extends AliyunONSBaseHandler {

    private List<OnsGroupListResponse.SubscribeInfoDo> onsGroupList(String regionId, String instanceId, String groupId, String groupType) {
        IAcsClient client = getClient(regionId);
        OnsGroupListRequest request = new OnsGroupListRequest();
        request.setInstanceId(instanceId);
        if (Strings.isEmpty(groupId))
            request.setGroupId(groupId);
        if (Strings.isEmpty(groupType))
            request.setGroupType(groupType);
        try {
            OnsGroupListResponse response = client.getAcsResponse(request);
            return response == null ? Collections.emptyList() : response.getData();
        } catch (ClientException e) {
            log.error("查询ONSGroupId列表失败", e);
            return Collections.emptyList();
        }
    }

    public List<OnsGroupListResponse.SubscribeInfoDo> queryOnsGroupList(AliyunONSParam.QueryGroupList param) {
        return onsGroupList(param.getRegionId(), param.getInstanceId(), null, null);
    }

    public OnsGroupListResponse.SubscribeInfoDo queryOnsGroup(AliyunONSParam.QueryGroup param) {
        List<OnsGroupListResponse.SubscribeInfoDo> groupList = onsGroupList(param.getRegionId(), param.getInstanceId(), param.getGroupId(), param.getGroupType());
        if (CollectionUtils.isEmpty(groupList))
            return null;
        return groupList.get(0);
    }

    public OnsGroupSubDetailResponse.Data queryOnsGroupSubDetail(AliyunONSParam.QueryGroupSubDetail param) {
        IAcsClient client = getClient(param.getRegionId());
        OnsGroupSubDetailRequest request = new OnsGroupSubDetailRequest();
        request.setInstanceId(param.getInstanceId());
        request.setGroupId(param.getGroupId());
        try {
            OnsGroupSubDetailResponse response = client.getAcsResponse(request);
            return response == null ? null : response.getData();
        } catch (ClientException e) {
            log.error("查询ONSGroupId订阅信息失败，groupId:{}", param.getGroupId(), e);
            return null;
        }
    }

    public Boolean onsGroupCreate(AliyunONSParam.GroupCreate param) {
        IAcsClient client = getClient(param.getRegionId());
        OnsGroupCreateRequest request = new OnsGroupCreateRequest();
        request.setInstanceId(param.getInstanceId());
        request.setGroupId(param.getGroupId());
        request.setGroupType(param.getGroupType());
        request.setRemark(param.getRemark());
        try {
            OnsGroupCreateResponse response = client.getAcsResponse(request);
            return response != null;
        } catch (ClientException e) {
            log.error("创建ONSGroupId失败，groupId:{}", param.getGroupId(), e);
            return false;
        }
    }

    public OnsConsumerStatusResponse.Data onsGroupStatus(AliyunONSParam.QueryGroupSubDetail param, Boolean needDetail) {
        IAcsClient client = getClient(param.getRegionId());
        OnsConsumerStatusRequest request = new OnsConsumerStatusRequest();
        request.setInstanceId(param.getInstanceId());
        request.setGroupId(param.getGroupId());
        request.setDetail(needDetail);
        try {
            OnsConsumerStatusResponse response = client.getAcsResponse(request);
            return response == null ? null : response.getData();
        } catch (ClientException e) {
            log.error("查询ONSGroupId详细信息失败，groupId:{}", param.getGroupId(), e);
            return null;
        }
    }

}
