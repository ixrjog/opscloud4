package com.baiyi.opscloud.decorator.aliyun.ons;

import com.alibaba.fastjson.JSONArray;
import com.aliyuncs.ons.model.v20190214.OnsConsumerStatusResponse;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsGroupAlarm;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.vo.cloud.AliyunONSVO;
import com.baiyi.opscloud.facade.ServerBaseFacade;
import com.baiyi.opscloud.service.aliyun.ons.OcAliyunOnsGroupAlarmService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/17 5:09 下午
 * @Since 1.0
 */

@Component("AliyunONSGroupDecorator")
public class AliyunONSGroupDecorator {

    @Resource
    private OcAliyunOnsGroupAlarmService ocAliyunOnsGroupAlarmService;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private OcServerService ocServerService;

    private AliyunONSVO.Group decoratorVO(OcAliyunOnsGroup ocAliyunOnsGroup) {
        AliyunONSVO.Group group = BeanCopierUtils.copyProperties(ocAliyunOnsGroup, AliyunONSVO.Group.class);
        group.setCreateTime(new Date(ocAliyunOnsGroup.getCreateTime()));
        OcAliyunOnsGroupAlarm alarm = ocAliyunOnsGroupAlarmService.queryOcAliyunOnsGroupByOnsGroupId(ocAliyunOnsGroup.getId());
        if (alarm != null) {
            group.setAlarmUserList(ocUserService.queryOcUserByIdList(JSONArray.parseArray(alarm.getAlarmUserList(), Integer.class)));
            group.setAlarmStatus(alarm.getAlarmStatus());
        }

        return group;
    }

    public List<AliyunONSVO.Group> decoratorVOList(List<OcAliyunOnsGroup> ocAliyunOnsGroupList) {
        List<AliyunONSVO.Group> groupList = Lists.newArrayListWithCapacity(ocAliyunOnsGroupList.size());
        ocAliyunOnsGroupList.forEach(ocAliyunOnsGroup -> groupList.add(decoratorVO(ocAliyunOnsGroup)));
        return groupList;
    }


    public AliyunONSVO.GroupStatus decoratorStatusVO(OnsConsumerStatusResponse.Data data) {
        AliyunONSVO.GroupStatus groupStatus = BeanCopierUtils.copyProperties(data, AliyunONSVO.GroupStatus.class);
        if (data.getLastTimestamp() != null)
            groupStatus.setLastTimestamp(new Date(data.getLastTimestamp()));
        groupStatus.setConnectionSet(decoratorGroupConnectionVOList(data.getConnectionSet()));
        return groupStatus;
    }

    private AliyunONSVO.GroupConnection decoratorGroupConnectionVO(OnsConsumerStatusResponse.Data.ConnectionDo connectionDo) {
        AliyunONSVO.GroupConnection groupConnection = BeanCopierUtils.copyProperties(connectionDo, AliyunONSVO.GroupConnection.class);
        OcServer ocServer = ocServerService.queryOcServerByIp(groupConnection.getClientAddr());
        if (ocServer != null) {
            groupConnection.setHostName(ServerBaseFacade.acqServerName(ocServer));
        }
        return groupConnection;
    }

    private List<AliyunONSVO.GroupConnection> decoratorGroupConnectionVOList(List<OnsConsumerStatusResponse.Data.ConnectionDo> connectionDoList) {
        List<AliyunONSVO.GroupConnection> connectionList = Lists.newArrayListWithCapacity(connectionDoList.size());
        connectionDoList.forEach(connectionDo -> connectionList.add(decoratorGroupConnectionVO(connectionDo)));
        return connectionList;
    }
}
