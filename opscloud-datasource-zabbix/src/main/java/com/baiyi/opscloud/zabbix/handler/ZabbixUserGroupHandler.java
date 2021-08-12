package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserGroup;
import com.baiyi.opscloud.zabbix.handler.base.ZabbixServer;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequest;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.http.ZabbixFilter;
import com.baiyi.opscloud.zabbix.http.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.zabbix.handler.base.ZabbixServer.ApiConstant.*;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/28 1:23 下午
 * @Since 1.0
 */

@Component
public class ZabbixUserGroupHandler {

    @Resource
    private ZabbixServer zabbixHandler;

    private interface UserGroupAPIMethod {
        String GET = "usergroup.get";
    }

    private SimpleZabbixRequestBuilder queryRequestBuilder() {
        return SimpleZabbixRequestBuilder.builder()
                .method(UserGroupAPIMethod.GET);
    }

    public List<ZabbixUserGroup> listGroups(DsZabbixConfig.Zabbix zabbix) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixUserGroup.class);
    }

    public List<ZabbixUserGroup> listGroupsByUser(DsZabbixConfig.Zabbix zabbix, ZabbixUser user) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .paramEntry(USER_IDS, user.getUserId())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixUserGroup.class);
    }

    public ZabbixUserGroup getGroupById(DsZabbixConfig.Zabbix zabbix, String groupId) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .paramEntry(USER_GROUP_IDS, groupId)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        List<ZabbixUserGroup> groups = ZabbixMapper.mapperList(data.get(RESULT), ZabbixUserGroup.class);
        if (CollectionUtils.isEmpty(groups))
            throw new RuntimeException("ZabbixUserGroup不存在");
        return groups.get(0);
    }

    public ZabbixUserGroup getGroupByName(DsZabbixConfig.Zabbix zabbix, String usergroup) {
        ZabbixFilter filter = ZabbixFilterBuilder.builder()
                .putEntry("name", usergroup)
                .build();

        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserGroupAPIMethod.GET)
                .paramEntry("status", 0)
                .filter(filter)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        List<ZabbixUserGroup> groups = ZabbixMapper.mapperList(data.get(RESULT), ZabbixUserGroup.class);
        if (CollectionUtils.isEmpty(groups))
            return null;
        return groups.get(0);
    }

}
