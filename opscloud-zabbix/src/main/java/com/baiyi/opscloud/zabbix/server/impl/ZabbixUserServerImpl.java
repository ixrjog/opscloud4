package com.baiyi.opscloud.zabbix.server.impl;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.zabbix.api.UserAPI;
import com.baiyi.opscloud.zabbix.api.UsergroupAPI;
import com.baiyi.opscloud.zabbix.builder.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.entry.*;
import com.baiyi.opscloud.zabbix.handler.ZabbixHandler;
import com.baiyi.opscloud.zabbix.http.ZabbixBaseRequest;
import com.baiyi.opscloud.zabbix.builder.ZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.param.ZabbixFilter;
import com.baiyi.opscloud.zabbix.param.ZabbixRequestParams;
import com.baiyi.opscloud.zabbix.mapper.ZabbixIdsMapper;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMediaMapper;
import com.baiyi.opscloud.zabbix.mapper.ZabbixUserMapper;
import com.baiyi.opscloud.zabbix.mapper.ZabbixUsergroupMapper;
import com.baiyi.opscloud.zabbix.server.ZabbixHostgroupServer;
import com.baiyi.opscloud.zabbix.server.ZabbixServer;
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

    public static final String ZABBIX_USRGRPS = "usrgrps";

    public static final String ZABBIX_MEDIAS = "medias";

    @Resource
    private ZabbixHandler zabbixHandler;

    @Resource
    private ZabbixServer zabbixServer;

    @Resource
    private ZabbixHostgroupServer zabbixHostgroupServer;

    @Override
    public ZabbixUser getUser(String username) {
        try {
            ZabbixFilter filter = ZabbixFilterBuilder.newBuilder()
                    .putEntry("alias", username)
                    .build();

            ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                    .method(UserAPI.GET)
                    .paramEntryByFilter(filter)
                    .build();
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixUserMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT)).get(0);
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public BusinessWrapper getUserUsrgrps(String username) {
        ZabbixFilter filter = ZabbixFilterBuilder.newBuilder()
                .putEntry("alias", username)
                .build();

        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(UserAPI.GET)
                .paramEntryByFilter(filter)
                .paramEntry("selectUsrgrps", "extend")
                .build();
        JsonNode jsonNode = null;
        try {
            jsonNode = zabbixHandler.api(request);
            List<ZabbixUsergroup> usergroups = new ZabbixUsergroupMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get(0).get(ZABBIX_USRGRPS));
            return new BusinessWrapper<>(usergroups);
        } catch (Exception ignored) {
        }
        return zabbixServer.result(jsonNode);
    }

    @Override
    public BusinessWrapper getUserMedias(String username) {
        ZabbixFilter filter = ZabbixFilterBuilder.newBuilder()
                .putEntry("alias", username)
                .build();

        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(UserAPI.GET)
                .paramEntryByFilter(filter)
                .paramEntry("selectMedias", "extend")
                .build();
        JsonNode jsonNode = null;
        try {
            jsonNode = zabbixHandler.api(request);
            List<ZabbixMedia> medias = new ZabbixMediaMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get(0).get(ZABBIX_MEDIAS));
            return new BusinessWrapper<>(medias);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zabbixServer.result(jsonNode);
    }

    @Override
    public List<ZabbixUser> getAllZabbixUser() {
        try {
            ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                    .method(UserAPI.GET)
                    .build();
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixUserMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT));
        } catch (Exception e) {
        }
        return Collections.emptyList();
    }

    @Override
    public BusinessWrapper<Boolean> createUser(ZabbixUser user, List<ZabbixUserMedia> mediaList, List<Map<String, String>> usrgrps) {
        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(UserAPI.CREATE)
                .paramEntry("alias", user.getAlias())
                .paramEntry("name", user.getName())
                .paramEntry("passwd", UUID.randomUUID())
                .paramEntry("usrgrps", usrgrps)
                .paramEntrySkipEmpty("user_medias", mediaList)
                .build();
        JsonNode jsonNode = null;
        try {
            jsonNode = zabbixHandler.api(request);
            String userid = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("userids")).get(0);
            if (!StringUtils.isEmpty(userid))
                return BusinessWrapper.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zabbixServer.result(jsonNode);
    }

    @Override
    public BusinessWrapper<Boolean> updateUser(ZabbixUser user, List<ZabbixUserMedia> mediaList, List<Map<String, String>> usrgrps) {
        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(UserAPI.UPDATE)
                .paramEntry("userid", user.getUserid())
                .paramEntry("name", user.getName())
                .paramEntry("usrgrps", usrgrps)
                .paramEntrySkipEmpty("user_medias", mediaList)
                .build();
        JsonNode jsonNode = null;
        try {
            jsonNode = zabbixHandler.api(request);
            String userid = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("userids")).get(0);
            if (!StringUtils.isEmpty(userid))
                return BusinessWrapper.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zabbixServer.result(jsonNode);
    }

    @Override
    public BusinessWrapper<Boolean> deleteUser(String username) {
        ZabbixUser user = getUser(username);
        if (user == null) return BusinessWrapper.SUCCESS;
        // 数组形参数 https://www.zabbix.com/documentation/2.2/manual/api/reference/user/delete
        ZabbixRequestParams request = ZabbixRequestParams.builder()
                .method(UserAPI.DELETE.getMethod())
                .params(new String[]{user.getUserid()})
                .build();

        JsonNode jsonNode = null;
        try {
            jsonNode = zabbixHandler.api(request);
            String userid = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("userids")).get(0);
            if (!StringUtils.isEmpty(userid))
                return BusinessWrapper.SUCCESS;
        } catch (Exception ignored) {
        }
        return zabbixServer.result(jsonNode);
    }

    @Override
    public ZabbixUsergroup getUsergroup(String usergroup) {
        ZabbixFilter filter = ZabbixFilterBuilder.newBuilder()
                .putEntry("name", usergroup)
                .build();

        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(UsergroupAPI.GET)
                .paramEntry("status", 0)
                .paramEntryByFilter(filter)
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
        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(UsergroupAPI.CREATE)
                .paramEntry("name", usergroupName)
                .paramEntry("rights", rights)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            String usrgrpid = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("usrgrpids")).get(0);
            if (!StringUtils.isEmpty(usrgrpid)) {
                ZabbixUsergroup zabbixUsergroup = new ZabbixUsergroup();
                zabbixUsergroup.setUsrgrpid(usrgrpid);
                zabbixUsergroup.setName(usergroupName);
                zabbixServer.createAction(usergroupName);
                return zabbixUsergroup;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public ZabbixUsergroup updateUsergroup(String usergroupName, ZabbixHostgroup zabbixHostgroup) {
        ZabbixUsergroup usergroup = getUsergroup(usergroupName);
        if (usergroup == null) return createUsergroup(usergroupName, zabbixHostgroup);
        Map<String, String> rights = Maps.newHashMap();
        /**
         * Possible values:
         0 - access denied;
         2 - read-only access;
         3 - read-write access.
         */
        rights.put("permission", "2");
        rights.put("id", zabbixHostgroup.getGroupid());
        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(UsergroupAPI.UPDATE)
                .paramEntry("usrgrpid", usergroup.getUsrgrpid())
                .paramEntry("rights", rights)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            String usrgrpid = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("usrgrpids")).get(0);
            if (!StringUtils.isEmpty(usrgrpid)) {
                ZabbixUsergroup zabbixUsergroup = new ZabbixUsergroup();
                zabbixUsergroup.setUsrgrpid(usrgrpid);
                zabbixUsergroup.setName(usergroupName);
                zabbixServer.createAction(usergroupName);
                return zabbixUsergroup;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
