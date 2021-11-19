package com.baiyi.opscloud.zabbix.datasource.base;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.zabbix.http.DefaultZabbixClient;
import com.baiyi.opscloud.zabbix.v5.request.IZabbixRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/25 4:21 下午
 * @Since 1.0
 */

@Component
@RequiredArgsConstructor
public class ZabbixServer {

    public static final String AUTH_CACHE_KAY_PREFIX = "opscloud_v4_zabbix_auth";

    public interface ApiConstant {
        String RESULT = "result";
        String HOST_GROUP_IDS = "groupids";
        String HOST_IDS = "hostids";
        String EVENT_IDS = "eventids";
        String TEMPLATE_IDS = "templateids";
        String TRIGGER_IDS = "triggerids";
    }

    private final RedisUtil redisUtil;

    @Retryable(value = RuntimeException.class, maxAttempts = 2, backoff = @Backoff(delay = 1000, multiplier = 1.5))
    public JsonNode call(ZabbixConfig.Zabbix zabbix, IZabbixRequest request) {
        DefaultZabbixClient zabbixClient = buildZabbixClient(zabbix);
        return zabbixClient.call(request);
    }

    private DefaultZabbixClient buildZabbixClient(ZabbixConfig.Zabbix zabbix) {
        String auth = getAuth(zabbix);
        if (!StringUtils.isEmpty(auth))
            return new DefaultZabbixClient(zabbix, auth);
        DefaultZabbixClient zabbixClient = new DefaultZabbixClient(zabbix);
        auth = zabbixClient.getAuth();
        return zabbixClient;
    }



    private String getAuth(ZabbixConfig.Zabbix zabbix) {
        String key = Joiner.on("_").join(AUTH_CACHE_KAY_PREFIX, zabbix.getUrl());
        if (redisUtil.hasKey(key)) {
            redisUtil.get(key);
        }
        return null;
    }



}
