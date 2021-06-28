package com.baiyi.caesar.zabbix.handler;

import com.baiyi.caesar.common.datasource.config.DsZabbixConfig;
import com.baiyi.caesar.zabbix.entry.ZabbixUser;
import com.baiyi.caesar.zabbix.entry.ZabbixUserGroup;
import com.baiyi.caesar.zabbix.http.ZabbixRequest;
import com.baiyi.caesar.zabbix.http.ZabbixRequestBuilder;
import com.baiyi.caesar.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/28 1:23 下午
 * @Since 1.0
 */

@Component
public class ZabbixUserGroupHandler {

    @Resource
    private ZabbixHandler zabbixHandler;

    private interface Method {
        String QUERY_GROUP = "usergroup.get";
    }

    public List<ZabbixUserGroup> ListGroups(DsZabbixConfig.Zabbix zabbix) {
        ZabbixRequest request = ZabbixRequestBuilder.builder()
                .method(Method.QUERY_GROUP)
                // 在users属性中返回用户组中的用户。
                .paramEntry("selectUsers", "extend")
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get("result"),ZabbixUserGroup.class);
    }

    public List<ZabbixUserGroup> ListGroupsByUser(DsZabbixConfig.Zabbix zabbix, ZabbixUser user) {
        ZabbixRequest request = ZabbixRequestBuilder.builder()
                .method(Method.QUERY_GROUP)
                // 只返回包含给定用户的用户组。
                .paramEntry("userids", user.getUserId())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get("result"),ZabbixUserGroup.class);
    }
}
