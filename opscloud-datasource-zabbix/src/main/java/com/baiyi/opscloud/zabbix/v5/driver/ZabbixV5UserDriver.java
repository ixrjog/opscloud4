package com.baiyi.opscloud.zabbix.v5.driver;

import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.v5.driver.base.AbstractZabbixV5UserDriver;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixMedia;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixUser;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixUserGroup;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixRequestBuilder;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author baiyi
 * @Date 2021/11/19 2:30 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixV5UserDriver extends AbstractZabbixV5UserDriver {

    @CacheEvict(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_user_name_' + #username")
    public void evictByUsername(ZabbixConfig.Zabbix config, String username) {
        log.info("Evict cache Zabbix User: alias={}", username);
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_user_name_' + #username", unless = "#result == null")
    public ZabbixUser.User getByUsername(ZabbixConfig.Zabbix config, String username) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                // 在medias 属性返回用户使用的媒体。
                .putParam("selectMedias", "extend")
                .filter(ZabbixFilterBuilder.builder()
                        .putEntry("alias", username)
                        .build())
                .build();
        ZabbixUser.QueryUserResponse response = queryHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult())) {
            return null;
        }
        return response.getResult().getFirst();
    }

    public List<ZabbixUser.User> list(ZabbixConfig.Zabbix config) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("selectMedias", "extend")
                .build();
        ZabbixUser.QueryUserResponse response = queryHandle(config, request);
        return response.getResult();
    }

    public List<ZabbixUser.User> listByGroup(ZabbixConfig.Zabbix config, ZabbixUserGroup.UserGroup userGroup) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("selectMedias", "extend")
                .putParam("usrgrpids", userGroup.getUsrgrpid())
                .build();
        ZabbixUser.QueryUserResponse response = queryHandle(config, request);
        return response.getResult();
    }

    public void create(ZabbixConfig.Zabbix config, ZabbixUser.User user, List<ZabbixMedia.Media> medias, List<Map<String, String>> usrgrps) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("alias", user.getAlias())
                .putParam("name", user.getName())
                .putParam("passwd", UUID.randomUUID())
                .putParam("usrgrps", usrgrps)
                .putParamSkipEmpty("user_medias", medias)
                .build();
        ZabbixUser.CreateUserResponse response = createHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult().getUserids())) {
            log.error("Create Zabbix User error: name={}", user.getName());
        }
    }

    /**
     * 更新用户信息 需要userid
     *
     * @param config
     * @param user
     * @param usrgrps
     * @param mediaList
     */
    public void update(ZabbixConfig.Zabbix config, ZabbixUser.User user, List<Map<String, String>> usrgrps, List<ZabbixMedia.Media> mediaList) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("userid", user.getUserid())
                .putParam("name", user.getName())
                .putParam("usrgrps", usrgrps)
                .putParamSkipEmpty("user_medias", mediaList)
                .build();
        ZabbixUser.UpdateUserResponse response = updateHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult().getUserids())) {
            log.error("Update Zabbix User error: name={}", user.getName());
        }
    }

    /**
     * 更新用户信息 需要userid
     *
     * @param config
     * @param user
     * @param usrgrps
     */
    public void update(ZabbixConfig.Zabbix config, ZabbixUser.User user, List<Map<String, String>> usrgrps) {
        update(config, user, usrgrps, Lists.newArrayList());
    }

    public void delete(ZabbixConfig.Zabbix config, String username) {
        ZabbixUser.User user = getByUsername(config, username);
        if (user == null) {
            return;
        }
        // 数组形参数 https://www.zabbix.com/documentation/2.2/manual/api/reference/user/delete
        ZabbixRequest.DeleteRequest request = ZabbixRequest.DeleteRequest.builder()
                .params(new String[]{user.getUserid()})
                .build();
        ZabbixUser.DeleteUserResponse response = deleteHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult().getUserids())) {
            log.error("Delete Zabbix User error: username={}", username);
        }
    }

    @CacheEvict(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_user_userid_' + #userid")
    public void evictById(ZabbixConfig.Zabbix config, String userid) {
        log.info("Evict cache Zabbix User: userid={}", userid);
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_user_userid_' + #userid", unless = "#result == null")
    public ZabbixUser.User getById(ZabbixConfig.Zabbix config, String userid) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("selectMedias", "extend")
                .putParam("userids", userid)
                .build();
        ZabbixUser.QueryUserResponse response = queryHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult())) {
            return null;
        }
        return response.getResult().getFirst();
    }

}