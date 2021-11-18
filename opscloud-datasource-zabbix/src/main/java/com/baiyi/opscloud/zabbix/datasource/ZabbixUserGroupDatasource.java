package com.baiyi.opscloud.zabbix.datasource;

import com.baiyi.opscloud.common.config.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.entity.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.entity.ZabbixUser;
import com.baiyi.opscloud.zabbix.entity.ZabbixUserGroup;
import com.baiyi.opscloud.zabbix.datasource.base.BaseZabbixDatasource;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequest;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.http.ZabbixFilter;
import com.baiyi.opscloud.zabbix.http.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.baiyi.opscloud.zabbix.datasource.base.ZabbixServer.ApiConstant.RESULT;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/28 1:23 下午
 * @Since 1.0
 */

@Component
public class ZabbixUserGroupDatasource extends BaseZabbixDatasource<ZabbixUserGroup> {

    private interface UserGroupAPIMethod {
        String GET = "usergroup.get";
        String CREATE = "usergroup.create";
    }

    public List<ZabbixUserGroup> list(ZabbixConfig.Zabbix zabbix) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserGroupAPIMethod.GET)
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixUserGroup.class);
    }

    public List<ZabbixUserGroup> listByUser(ZabbixConfig.Zabbix zabbix, ZabbixUser user) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserGroupAPIMethod.GET)
                .putParam("userids", user.getUserid())
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixUserGroup.class);
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.ZABBIX, key = "#zabbix.url + '_usergroup_usrgrpid_' + #usrgrpid", unless = "#result == null")
    public ZabbixUserGroup getById(ZabbixConfig.Zabbix zabbix, String usrgrpid) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserGroupAPIMethod.GET)
                .putParam("usrgrpids", usrgrpid)
                .build();
        JsonNode data = call(zabbix, request);
        return mapperListGetOne(data.get(RESULT), ZabbixUserGroup.class);
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.ZABBIX, key = "#zabbix.url + '_usergroup_name_' + #usergroup", unless = "#result == null")
    public ZabbixUserGroup getByName(ZabbixConfig.Zabbix zabbix, String usergroup) {
        ZabbixFilter filter = ZabbixFilterBuilder.builder()
                .putEntry("name", usergroup)
                .build();
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserGroupAPIMethod.GET)
                .putParam("status", 0)
                .filter(filter)
                .build();
        JsonNode data = call(zabbix, request);
        return mapperListGetOne(data.get(RESULT), ZabbixUserGroup.class);
    }

    public ZabbixUserGroup create(ZabbixConfig.Zabbix zabbix, String usergroup, ZabbixHostGroup zabbixHostGroup) {
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
                .putParam("name", usergroup)
                .putParam("rights", rights)
                .build();
        JsonNode data = call(zabbix, request);
        String usrgrpid = ZabbixMapper.mapperList(data.get(RESULT).get("usrgrpids"), String.class).get(0);
        if (StringUtils.isEmpty(usrgrpid))
            return null;
        return getById(zabbix, usrgrpid);
    }

}
