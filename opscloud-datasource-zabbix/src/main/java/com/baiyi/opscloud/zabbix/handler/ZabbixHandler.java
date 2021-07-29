package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.StringToDurationUtil;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import com.baiyi.opscloud.zabbix.http.DefaultZabbixClient;
import com.baiyi.opscloud.zabbix.http.ZabbixRequest;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/25 4:21 下午
 * @Since 1.0
 */

@Component
public class ZabbixHandler {

    private static final String ZABBIX_AUTH_CACHE_KAY = "opscloud:v4:zabbix_auth";

    public interface ApiConstant {
        String RESULT = "result";
        String USER_IDS = "userids";
        String USER_GROUP_IDS = "usrgrpids";
        String HOST_GROUP_IDS = "groupids";
        String HOST_IDS = "hostids";
        String HOST_ID = "hostid";
        String EVENT_IDS = "eventids";
        String TEMPLATE_IDS = "templateids";
        String TRIGGER_IDS = "triggerids";
    }

    @Resource
    private RedisUtil redisUtil;

    @Retryable(value = RuntimeException.class, maxAttempts = 4, backoff = @Backoff(delay = 1000, multiplier = 1.5))
    public JsonNode call(DsZabbixConfig.Zabbix zabbix, ZabbixRequest request) {
        DefaultZabbixClient zabbixClient = getZabbixClient(zabbix);
        return zabbixClient.call(request);
    }

    private DefaultZabbixClient getZabbixClient(DsZabbixConfig.Zabbix zabbix) {
        if (redisUtil.hasKey(ZABBIX_AUTH_CACHE_KAY)) {
            return new DefaultZabbixClient(zabbix, (String) redisUtil.get(ZABBIX_AUTH_CACHE_KAY));
        }
        DefaultZabbixClient zabbixClient = new DefaultZabbixClient(zabbix);
        ZabbixUser zabbixUser = zabbixClient.login();
        redisUtil.set(ZABBIX_AUTH_CACHE_KAY, zabbixUser.getSessionId(), getAuthCacheTime(zabbixUser));
        return zabbixClient;
    }

    private long getAuthCacheTime(ZabbixUser zabbixUser) {
        if (1 == zabbixUser.getAutoLogin()) {
            // 缓存7天
            return 7 * 24 * 60 * 60 * 60;
        }
        Duration cacheTime = StringToDurationUtil.parse(zabbixUser.getAutoLogout());
        return cacheTime.getSeconds();
    }

}
