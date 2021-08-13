package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixMedia;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserGroup;
import com.baiyi.opscloud.zabbix.handler.base.BaseZabbixHandler;
import com.baiyi.opscloud.zabbix.http.*;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
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

    public void create(DsZabbixConfig.Zabbix zabbix, ZabbixUser user, List<ZabbixMedia> medias, List<Map<String, String>> usrgrps) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserAPIMethod.CREATE)
                .paramEntry("alias", user.getAlias())
                .paramEntry("name", user.getName())
                .paramEntry("passwd", UUID.randomUUID())
                .paramEntry("usrgrps", usrgrps)
                .paramEntrySkipEmpty("user_medias", medias)
                .build();
        JsonNode data = call(zabbix, request);
        try {
            List<Integer> userIds = ZabbixMapper.mapperList(data.get(RESULT).get("userids"), Integer.class);
            // TODO 先不处理
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新用户信息 需要userid
     *
     * @param zabbix
     * @param user
     * @param mediaList
     * @param usrgrps
     */
    public void update(DsZabbixConfig.Zabbix zabbix, ZabbixUser user, List<ZabbixMedia> mediaList, List<Map<String, String>> usrgrps) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserAPIMethod.UPDATE)
                .paramEntry("userid", user.getUserid())
                .paramEntry("name", user.getName())
                .paramEntry("usrgrps", usrgrps)
                .paramEntrySkipEmpty("user_medias", mediaList)
                .build();
        JsonNode data = call(zabbix, request);
        try {
            List<Integer> userIds = ZabbixMapper.mapperList(data.get(RESULT).get("userids"), Integer.class);
            // TODO 先不处理
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(DsZabbixConfig.Zabbix zabbix, String username) {
        ZabbixUser zabbixUser = getByUsername(zabbix, username);
        if (zabbixUser == null) return ;
        // 数组形参数 https://www.zabbix.com/documentation/2.2/manual/api/reference/user/delete
        ZabbixDeleteRequest request  = ZabbixDeleteRequest.builder()
                .method(UserAPIMethod.DELETE)
                .params(new String[]{ zabbixUser.getUserid()})
                .build();
        JsonNode data = call(zabbix, request);
        try {
            List<Integer> userIds = ZabbixMapper.mapperList(data.get(RESULT).get("userids"), Integer.class);
            // TODO 先不处理
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ZabbixUser getById(DsZabbixConfig.Zabbix zabbix, String userId) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(UserAPIMethod.GET)
                .paramEntry("selectMedias", "extend")
                .paramEntry("userids", userId)
                .build();
        JsonNode data = call(zabbix, request);
        return mapperListGetOne(data.get(RESULT), ZabbixUser.class);
    }

}
