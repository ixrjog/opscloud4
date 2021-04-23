package com.baiyi.opscloud.cloud.mq.impl;

import com.aliyuncs.ons.model.v20190214.OnsConsumerStatusResponse;
import com.aliyuncs.ons.model.v20190214.OnsGroupListResponse;
import com.aliyuncs.ons.model.v20190214.OnsGroupSubDetailResponse;
import com.baiyi.opscloud.aliyun.ons.AliyunONSGroup;
import com.baiyi.opscloud.cloud.mq.AliyunONSGroupCenter;
import com.baiyi.opscloud.cloud.mq.builder.AliyunONSGroupBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsGroupAlarm;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;
import com.baiyi.opscloud.service.aliyun.ons.OcAliyunOnsGroupAlarmService;
import com.baiyi.opscloud.service.aliyun.ons.OcAliyunOnsGroupService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/9 3:56 下午
 * @Since 1.0
 */

@Slf4j
@Component("AliyunONSGroupCenter")
public class AliyunONSGroupCenterImpl implements AliyunONSGroupCenter {

    @Resource
    private AliyunONSGroup aliyunONSGroup;

    @Resource
    private OcAliyunOnsGroupService ocAliyunOnsGroupService;

    @Resource
    private OcAliyunOnsGroupAlarmService ocAliyunOnsGroupAlarmService;

    @Override
    public Boolean syncONSGroup(AliyunONSParam.QueryGroupList param) {
        HashMap<String, OcAliyunOnsGroup> map = getGroupMap(param);
        List<OnsGroupListResponse.SubscribeInfoDo> instanceList = aliyunONSGroup.queryOnsGroupList(param);
        instanceList.forEach(group -> {
            saveGroup(group);
            map.remove(group.getGroupId());
        });
        delGroupByMap(map);
        return true;
    }

    private HashMap<String, OcAliyunOnsGroup> getGroupMap(AliyunONSParam.QueryGroupList param) {
        List<OcAliyunOnsGroup> groupList = ocAliyunOnsGroupService.queryOcAliyunOnsGroupByInstanceId(param.getInstanceId());
        HashMap<String, OcAliyunOnsGroup> map = Maps.newHashMap();
        groupList.forEach(group -> map.put(group.getGroupId(), group));
        return map;
    }

    private Boolean saveGroup(OnsGroupListResponse.SubscribeInfoDo group) {
        OcAliyunOnsGroup ocAliyunOnsGroup = ocAliyunOnsGroupService.queryOcAliyunOnsGroupByInstanceIdAndGroupId(group.getInstanceId(), group.getGroupId());
        OcAliyunOnsGroup newOcAliyunOnsGroup = AliyunONSGroupBuilder.build(group);
        if (ocAliyunOnsGroup == null) {
            try {
                ocAliyunOnsGroupService.addOcAliyunOnsGroup(newOcAliyunOnsGroup);
                return true;
            } catch (Exception e) {
                newOcAliyunOnsGroup.setId(ocAliyunOnsGroup.getId());
                ocAliyunOnsGroupService.updateOcAliyunOnsGroup(newOcAliyunOnsGroup);
                log.error("新增阿里云onsGroup信息失败，Group:{}", group.getGroupId(), e);
                return false;
            }
        }
        return true;
    }

    private void delGroupByMap(HashMap<String, OcAliyunOnsGroup> map) {
        map.forEach((key, value) -> {
            ocAliyunOnsGroupService.deleteOcAliyunOnsGroupById(value.getId());
            OcAliyunOnsGroupAlarm aliyunOnsGroupAlarm = ocAliyunOnsGroupAlarmService.queryOcAliyunOnsGroupByOnsGroupId(value.getId());
            if (aliyunOnsGroupAlarm != null)
                ocAliyunOnsGroupAlarmService.deleteOcAliyunOnsGroupAlarmById(aliyunOnsGroupAlarm.getId());
        });
    }

    @Override
    public OnsGroupSubDetailResponse.Data queryOnsGroupSubDetail(AliyunONSParam.QueryGroupSubDetail param) {
        return aliyunONSGroup.queryOnsGroupSubDetail(param);
    }

    @Override
    public Boolean onsGroupCreate(AliyunONSParam.GroupCreate param) {
        return aliyunONSGroup.onsGroupCreate(param);
    }

    @Override
    public Boolean saveGroup(AliyunONSParam.QueryGroup param) {
        OnsGroupListResponse.SubscribeInfoDo group = aliyunONSGroup.queryOnsGroup(param);
        if (group != null) {
            return saveGroup(group);
        }
        return false;
    }

    @Override
    public OnsConsumerStatusResponse.Data onsGroupStatus(AliyunONSParam.QueryGroupSubDetail param, Boolean needDetail) {
        try {
            return aliyunONSGroup.onsGroupStatus(param, needDetail);
        } catch (RuntimeException e) {
            log.error("重试查询ONS GroupId详细信息失败,groupId为:{}", param.getGroupId());
            return null;
        }
    }
}
