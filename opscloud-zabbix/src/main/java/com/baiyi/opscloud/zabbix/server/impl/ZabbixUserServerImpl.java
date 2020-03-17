package com.baiyi.opscloud.zabbix.server.impl;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostgroup;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserMedia;
import com.baiyi.opscloud.zabbix.entry.ZabbixUsergroup;
import com.baiyi.opscloud.zabbix.handler.ZabbixHandler;
import com.baiyi.opscloud.zabbix.http.ZabbixRequest;
import com.baiyi.opscloud.zabbix.http.ZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.http.ZabbixRequestParamsIds;
import com.baiyi.opscloud.zabbix.mapper.ZabbixIdsMapper;
import com.baiyi.opscloud.zabbix.mapper.ZabbixUserMapper;
import com.baiyi.opscloud.zabbix.mapper.ZabbixUsergroupMapper;
import com.baiyi.opscloud.zabbix.server.ZabbixHostgroupServer;
import com.baiyi.opscloud.zabbix.server.ZabbixUserServer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author baiyi
 * @Date 2020/1/8 1:02 下午
 * @Version 1.0
 */
@Component("ZabbixUserServer")
public class ZabbixUserServerImpl implements ZabbixUserServer {

    @Resource
    private ZabbixHandler zabbixHandler;


    @Resource
    private ZabbixHostgroupServer zabbixHostgroupServer;

    @Override
    public ZabbixUser getUser(String username) {
        try {
            Map<String, String> filter = Maps.newHashMap();
            filter.put("alias", username);
            ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                    .method("user.get")
                    .paramEntry("filter", filter)
                    .build();
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixUserMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT)).get(0);
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public List<ZabbixUser> getAllZabbixUser() {
        try {
            ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                    .method("user.get")
                    .build();
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixUserMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT));
        } catch (Exception e) {
        }
        return Collections.emptyList();
    }

    @Override
    public Boolean createUser(ZabbixUser user, List<ZabbixUserMedia> mediaList, List<Map<String, String>> usrgrps) {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("user.create")
                .paramEntry("alias", user.getAlias())
                .paramEntry("name", user.getName())
                .paramEntry("passwd", UUID.randomUUID())
                .paramEntry("usrgrps", usrgrps)
                .paramEntrySkipEmpty("user_medias", mediaList)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            String userid = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("userids")).get(0);
            if (!StringUtils.isEmpty(userid))
                return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean updateUser(ZabbixUser user, List<ZabbixUserMedia> mediaList, List<Map<String, String>> usrgrps) {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("user.update")
                .paramEntry("userid", user.getUserid())
                .paramEntry("name", user.getName())
                .paramEntry("usrgrps", usrgrps)
                .paramEntrySkipEmpty("user_medias", mediaList)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            String userid = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("userids")).get(0);
            if (!StringUtils.isEmpty(userid))
                return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean deleteUser(String username) {
        ZabbixUser user = getUser(username);
        if (user == null) return Boolean.TRUE;
        // 数组形参数 https://www.zabbix.com/documentation/2.2/manual/api/reference/user/delete
        String[] userids = new String[]{user.getUserid()};
        ZabbixRequestParamsIds request = new ZabbixRequestParamsIds();
        request.setMethod("user.delete");
        request.setId(1);
        request.setParams(userids);

        try {
            System.err.println(JSON.toJSONString(request));
            JsonNode jsonNode = zabbixHandler.api(request);
            String userid = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("userids")).get(0);
            if (!StringUtils.isEmpty(userid))
                return Boolean.TRUE;
        } catch (Exception e) {
        }
        return Boolean.FALSE;
    }

    @Override
    public ZabbixUsergroup getUsergroup(String usergroup) {
        Map<String, String> filter = Maps.newHashMap();
        filter.put("name", usergroup);
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("usergroup.get")
                .paramEntry("status", 0)
                .paramEntry("filter", filter)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixUsergroupMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT)).get(0);
        } catch (Exception e) {
            // 未取到用户组则创建  users_zookeeper  group_zookeeper
            ZabbixHostgroup zabbixHostgroup = zabbixHostgroupServer.createHostgroup(usergroup);
            return createUsergroup(usergroup, zabbixHostgroup);
        }
    }

    @Override
    public ZabbixUsergroup createUsergroup(String usergroupName, ZabbixHostgroup zabbixHostgroup) {
        // 创建用户组
        Map<String, String> rights = Maps.newHashMap();
        /**
         * Possible values:
         0 - access denied;
         2 - read-only access;
         3 - read-write access.
         */
        rights.put("permission", "2");
        rights.put("id", zabbixHostgroup.getGroupid());
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("usergroup.create")
                .paramEntry("name", usergroupName)
                .paramEntry("rights", rights)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            String usrgrpid = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("usrgrpids")).get(0);
            if (!StringUtils.isEmpty(usrgrpid)) {
                ZabbixUsergroup   zabbixUsergroup = new ZabbixUsergroup();
                zabbixUsergroup.setUsrgrpid(usrgrpid);
                zabbixUsergroup.setName(usergroupName);
                return zabbixUsergroup;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
