package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixMedia;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserGroup;
import com.baiyi.opscloud.zabbix.handler.base.BaseZabbixHandler;
import com.baiyi.opscloud.zabbix.http.*;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.baiyi.opscloud.zabbix.handler.base.ZabbixServer.ApiConstant.RESULT;

/**
 * @Author baiyi
 * @Date 2021/6/22 1:50 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixUserHandler extends BaseZabbixHandler<ZabbixUser> {

    private interface UserAPIMethod {
        String GET = "user.get";
        String CREATE = "user.create";
        String UPDATE = "user.update";
        String DELETE = "user.delete";
    }

    @CacheEvict(cacheNames = CachingConfig.Repositories.ZABBIX, key = "'user_alias_' + #username")
    public void evictByUsername(String username) {
        log.info("清除ZabbixUser缓存 : alias = {}", username);
    }

    @CacheEvict(cacheNames = CachingConfig.Repositories.ZABBIX, key = "'user_alias_' + #username")
    public ZabbixUser getByUsername(DsZabbixConfig.Zabbix zabbix, String username) {
        ZabbixFilter filter = ZabbixFilterBuilder.builder()
                .putEntry("alias", username)
                .build();
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserAPIMethod.GET)
                .paramEntry("selectMedias", "extend")   // 在medias 属性返回用户使用的媒体。
                .filter(filter)
                .build();
        JsonNode data = call(zabbix, request);
        return mapperListGetOne(data.get(RESULT), ZabbixUser.class);
    }

    public List<ZabbixUser> list(DsZabbixConfig.Zabbix zabbix) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserAPIMethod.GET)
                .paramEntry("selectMedias", "extend")
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixUser.class);
    }

    public List<ZabbixUser> listByGroup(DsZabbixConfig.Zabbix zabbix, ZabbixUserGroup userGroup) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserAPIMethod.GET)
                .paramEntry("selectMedias", "extend")
                .paramEntry("usrgrpids", userGroup.getUsrgrpid())
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixUser.class);
    }

    public void create(DsZabbixConfig.Zabbix zabbix, ZabbixUser zabbixUser, List<ZabbixMedia> medias, List<Map<String, String>> usrgrps) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserAPIMethod.CREATE)
                .paramEntry("alias", zabbixUser.getAlias())
                .paramEntry("name", zabbixUser.getName())
                .paramEntry("passwd", UUID.randomUUID())
                .paramEntry("usrgrps", usrgrps)
                .paramEntrySkipEmpty("user_medias", medias)
                .build();
        JsonNode data = call(zabbix, request);
        if (data.get(RESULT).get("userids").isEmpty()) {
            log.error("创建ZabbixUser失败: name = {}", zabbixUser.getName());
        } else {
            log.info("创建ZabbixUser: name = {}", zabbixUser.getName());
        }
    }

    /**
     * 更新用户信息 需要userid
     *
     * @param zabbix
     * @param zabbixUser
     * @param mediaList
     * @param usrgrps
     */
    public void update(DsZabbixConfig.Zabbix zabbix, ZabbixUser zabbixUser, List<Map<String, String>> usrgrps, List<ZabbixMedia> mediaList) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserAPIMethod.UPDATE)
                .paramEntry("userid", zabbixUser.getUserid())
                .paramEntry("name", zabbixUser.getName())
                .paramEntry("usrgrps", usrgrps)
                .paramEntrySkipEmpty("user_medias", mediaList)
                .build();
        JsonNode data = call(zabbix, request);
        if (data.get(RESULT).get("userids").isEmpty()) {
            log.error("更新ZabbixUser失败: name = {}", zabbixUser.getName());
        } else {
            log.info("更新ZabbixUser: name = {}", zabbixUser.getName());
        }
    }

    /**
     * 更新用户信息 需要userid
     *
     * @param zabbix
     * @param user
     * @param usrgrps
     */
    public void update(DsZabbixConfig.Zabbix zabbix, ZabbixUser user, List<Map<String, String>> usrgrps) {
        update(zabbix, user, usrgrps, Lists.newArrayList());
    }

    public void delete(DsZabbixConfig.Zabbix zabbix, String username) {
        ZabbixUser zabbixUser = getByUsername(zabbix, username);
        if (zabbixUser == null) return;
        // 数组形参数 https://www.zabbix.com/documentation/2.2/manual/api/reference/user/delete
        ZabbixDeleteRequest request = ZabbixDeleteRequest.builder()
                .method(UserAPIMethod.DELETE)
                .params(new String[]{zabbixUser.getUserid()})
                .build();
        JsonNode data = call(zabbix, request);
        try {
            List<Integer> userIds = ZabbixMapper.mapperList(data.get(RESULT).get("userids"), Integer.class);
            // TODO 先不处理
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @CacheEvict(cacheNames = CachingConfig.Repositories.ZABBIX, key = "'user_userid_' + #userid")
    public void evictById(String userid) {
        log.info("清除ZabbixUser缓存 : userid = {}", userid);
    }

    @CacheEvict(cacheNames = CachingConfig.Repositories.ZABBIX, key = "'user_userid_' + #userid")
    public ZabbixUser getById(DsZabbixConfig.Zabbix zabbix, String userid) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserAPIMethod.GET)
                .paramEntry("selectMedias", "extend")
                .paramEntry("userids", userid)
                .build();
        JsonNode data = call(zabbix, request);
        return mapperListGetOne(data.get(RESULT), ZabbixUser.class);
    }

}
