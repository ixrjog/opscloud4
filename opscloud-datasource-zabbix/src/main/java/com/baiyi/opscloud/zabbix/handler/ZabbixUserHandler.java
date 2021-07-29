package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserGroup;
import com.baiyi.opscloud.zabbix.http.ZabbixCommonRequest;
import com.baiyi.opscloud.zabbix.http.ZabbixCommonRequestBuilder;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.zabbix.handler.ZabbixHandler.ApiConstant.*;

/**
 * @Author baiyi
 * @Date 2021/6/22 1:50 下午
 * @Version 1.0
 */
@Component
public class ZabbixUserHandler {

    @Resource
    private ZabbixHandler zabbixHandler;

    private interface Method {
        String QUERY_USER = "user.get";
    }

    public List<ZabbixUser> listUsers(DsZabbixConfig.Zabbix zabbix) {
        ZabbixCommonRequest request = ZabbixCommonRequestBuilder.builder()
                .method(Method.QUERY_USER)
                // 在medias 属性返回用户使用的媒体。
//                .paramEntry("selectMedias", "extend")
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixUser.class);
    }

    public List<ZabbixUser> listUsersByGroup(DsZabbixConfig.Zabbix zabbix, ZabbixUserGroup userGroup) {
        ZabbixCommonRequest request = ZabbixCommonRequestBuilder.builder()
                .method(Method.QUERY_USER)
                .paramEntry(USER_GROUP_IDS, userGroup.getUserGroupId())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixUser.class);
    }

    public ZabbixUser getUserById(DsZabbixConfig.Zabbix zabbix, String userId) {
        ZabbixCommonRequest request = ZabbixCommonRequestBuilder.builder()
                .method(Method.QUERY_USER)
                .paramEntry(USER_IDS, userId)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        List<ZabbixUser> users = ZabbixMapper.mapperList(data.get(RESULT), ZabbixUser.class);
        if (CollectionUtils.isEmpty(users))
            throw new RuntimeException("ZabbixUser不存在");
        return users.get(0);
    }

}
