package com.baiyi.opscloud.zabbix.handler;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixMedia;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserGroup;
import com.baiyi.opscloud.zabbix.handler.base.ZabbixServer;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequest;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.baiyi.opscloud.zabbix.handler.base.ZabbixServer.ApiConstant.*;

/**
 * @Author baiyi
 * @Date 2021/6/22 1:50 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixUserHandler {

    @Resource
    private ZabbixServer zabbixHandler;

    private interface UserAPIMethod {
        String GET = "user.get";
        String CREATE = "user.create";
        String UPDATE = "user.update";
        String DELETE = "user.delete";
    }

    private SimpleZabbixRequestBuilder queryRequestBuilder() {
        return SimpleZabbixRequestBuilder.builder()
                .method(UserAPIMethod.GET)
                // 在medias 属性返回用户使用的媒体。
                .paramEntry("selectMedias", "extend");
    }

    public List<ZabbixUser> listUsers(DsZabbixConfig.Zabbix zabbix) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixUser.class);
    }

    public List<ZabbixUser> listUsersByGroup(DsZabbixConfig.Zabbix zabbix, ZabbixUserGroup userGroup) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .paramEntry(USER_GROUP_IDS, userGroup.getUserGroupId())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixUser.class);
    }

    public void createUser(DsZabbixConfig.Zabbix zabbix, ZabbixUser user, List<ZabbixMedia> medias, List<Map<String, String>> usrgrps) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .method(UserAPIMethod.CREATE)
                .paramEntry("alias", user.getAlias())
                .paramEntry("name", user.getName())
                .paramEntry("passwd", UUID.randomUUID())
                .paramEntry("usrgrps", usrgrps)
                .paramEntrySkipEmpty("user_medias", medias)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        List<Integer> userIds = ZabbixMapper.mapperList(data.get(RESULT).get("userids"), Integer.class);
        log.info("创建ZabbixUser userIds = {}", JSON.toJSONString(userIds));
    }

    public ZabbixUser getUserById(DsZabbixConfig.Zabbix zabbix, String userId) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .paramEntry(USER_IDS, userId)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        List<ZabbixUser> users = ZabbixMapper.mapperList(data.get(RESULT), ZabbixUser.class);
        if (CollectionUtils.isEmpty(users))
            throw new RuntimeException("ZabbixUser不存在");
        return users.get(0);
    }

}
