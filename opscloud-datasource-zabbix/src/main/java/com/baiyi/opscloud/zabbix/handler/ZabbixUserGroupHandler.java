package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserGroup;
import com.baiyi.opscloud.zabbix.handler.base.BaseZabbixHandler;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequest;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.http.ZabbixFilter;
import com.baiyi.opscloud.zabbix.http.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.baiyi.opscloud.zabbix.handler.base.ZabbixServer.ApiConstant.RESULT;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/28 1:23 下午
 * @Since 1.0
 */

@Component
public class ZabbixUserGroupHandler extends BaseZabbixHandler<ZabbixUserGroup> {

    private interface UserGroupAPIMethod {
        String GET = "usergroup.get";
        String CREATE = "usergroup.create";
    }

    public List<ZabbixUserGroup> list(DsZabbixConfig.Zabbix zabbix) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserGroupAPIMethod.GET)
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixUserGroup.class);
    }

    public List<ZabbixUserGroup> listByUser(DsZabbixConfig.Zabbix zabbix, ZabbixUser user) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserGroupAPIMethod.GET)
                .paramEntry("userids", user.getUserid())
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixUserGroup.class);
    }

    public ZabbixUserGroup getById(DsZabbixConfig.Zabbix zabbix, String groupId) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserGroupAPIMethod.GET)
                .paramEntry("usrgrpids", groupId)
                .build();
        JsonNode data = call(zabbix, request);
        return mapperListGetOne(data.get(RESULT), ZabbixUserGroup.class);
    }

    public ZabbixUserGroup getByName(DsZabbixConfig.Zabbix zabbix, String usergroup) {
        ZabbixFilter filter = ZabbixFilterBuilder.builder()
                .putEntry("name", usergroup)
                .build();
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserGroupAPIMethod.GET)
                .paramEntry("status", 0)
                .filter(filter)
                .build();
        JsonNode data = call(zabbix, request);
        return mapperListGetOne(data.get(RESULT), ZabbixUserGroup.class);
    }

    public ZabbixUserGroup create(DsZabbixConfig.Zabbix zabbix, String usergroup, ZabbixHostGroup zabbixHostGroup) {
        // 创建用户组
        Map<String, String> rights = Maps.newHashMap();
        /**
         * Possible values:
         0 - access denied;
         2 - read-only access;
         3 - read-write access.
         */
        rights.put("permission", "2");
        rights.put("id", zabbixHostGroup.getGroupid());
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserGroupAPIMethod.CREATE)
                .paramEntry("name", usergroup)
                .paramEntry("rights", rights)
                .build();
        JsonNode data = call(zabbix, request);
        String usrgrpid = ZabbixMapper.mapperList(data.get(RESULT).get("usrgrpids"), String.class).get(0);
        if (StringUtils.isEmpty(usrgrpid))
            return null;
        return getById(zabbix, usrgrpid);
    }


}
