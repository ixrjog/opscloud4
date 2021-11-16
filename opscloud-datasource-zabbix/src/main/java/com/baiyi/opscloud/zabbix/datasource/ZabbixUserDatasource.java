package com.baiyi.opscloud.zabbix.datasource;

import com.baiyi.opscloud.common.config.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixMedia;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserGroup;
import com.baiyi.opscloud.zabbix.datasource.base.BaseZabbixDatasource;
import com.baiyi.opscloud.zabbix.http.*;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.baiyi.opscloud.zabbix.datasource.base.ZabbixServer.ApiConstant.RESULT;

/**
 * @Author baiyi
 * @Date 2021/6/22 1:50 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixUserDatasource extends BaseZabbixDatasource<ZabbixUser> {

    private interface UserAPIMethod {
        String GET = "user.get";
        String CREATE = "user.create";
        String UPDATE = "user.update";
        String DELETE = "user.delete";
    }

    @CacheEvict(cacheNames = CachingConfiguration.Repositories.ZABBIX, key = "#zabbix.url + '_user_name_' + #username")
    public void evictByUsername(ZabbixConfig.Zabbix zabbix, String username) {
        log.info("清除ZabbixUser缓存 : alias = {}", username);
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.ZABBIX, key = "#zabbix.url + '_user_name_' + #username", unless = "#result == null")
    public ZabbixUser getByUsername(ZabbixConfig.Zabbix zabbix, String username) {
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

    public List<ZabbixUser> list(ZabbixConfig.Zabbix zabbix) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserAPIMethod.GET)
                .paramEntry("selectMedias", "extend")
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixUser.class);
    }

    public List<ZabbixUser> listByGroup(ZabbixConfig.Zabbix zabbix, ZabbixUserGroup userGroup) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserAPIMethod.GET)
                .paramEntry("selectMedias", "extend")
                .paramEntry("usrgrpids", userGroup.getUsrgrpid())
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixUser.class);
    }

    public void create(ZabbixConfig.Zabbix zabbix, ZabbixUser zabbixUser, List<ZabbixMedia> medias, List<Map<String, String>> usrgrps) {
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
    public void update(ZabbixConfig.Zabbix zabbix, ZabbixUser zabbixUser, List<Map<String, String>> usrgrps, List<ZabbixMedia> mediaList) {
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
    public void update(ZabbixConfig.Zabbix zabbix, ZabbixUser user, List<Map<String, String>> usrgrps) {
        update(zabbix, user, usrgrps, Lists.newArrayList());
    }

    public void delete(ZabbixConfig.Zabbix zabbix, String username) {
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

    @CacheEvict(cacheNames = CachingConfiguration.Repositories.ZABBIX, key = "#zabbix.url + '_user_userid_' + #userid")
    public void evictById(ZabbixConfig.Zabbix zabbix, String userid) {
        log.info("清除ZabbixUser缓存 : userid = {}", userid);
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.ZABBIX, key = "#zabbix.url + '_user_userid_' + #userid", unless = "#result == null")
    public ZabbixUser getById(ZabbixConfig.Zabbix zabbix, String userid) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserAPIMethod.GET)
                .paramEntry("selectMedias", "extend")
                .paramEntry("userids", userid)
                .build();
        JsonNode data = call(zabbix, request);
        return mapperListGetOne(data.get(RESULT), ZabbixUser.class);
    }

}
