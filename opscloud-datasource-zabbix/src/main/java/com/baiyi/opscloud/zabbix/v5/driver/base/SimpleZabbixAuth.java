package com.baiyi.opscloud.zabbix.v5.driver.base;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixLogin;
import com.baiyi.opscloud.zabbix.v5.feign.ZabbixLoginFeign;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixRequestBuilder;
import com.google.common.base.Joiner;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/11/17 10:53 上午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class SimpleZabbixAuth {

    public static final String AUTH_CACHE_KAY_PREFIX = "opscloud_v5_zabbix_auth";

    private final RedisUtil redisUtil;

    private String buildKey(ZabbixConfig.Zabbix config) {
        return Joiner.on("_").join(AUTH_CACHE_KAY_PREFIX, config.getUrl());
    }

    public String getAuth(ZabbixConfig.Zabbix config) {
        String key = buildKey(config);
        if (redisUtil.hasKey(key)) {
            return (String) redisUtil.get(key);
        } else {
            ZabbixLogin.LoginAuth loginAuth = login(config);
            cacheAuth(key, loginAuth.getResult());
            return loginAuth.getResult();
        }
    }

    private void cacheAuth(String key, String auth) {
        // 缓存14分钟
        redisUtil.set(key, auth, NewTimeUtil.MINUTE_TIME * 14 / 1000);
    }

    private ZabbixLogin.LoginAuth login(ZabbixConfig.Zabbix config) {
        ZabbixLoginFeign zabbixAPI = Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(ZabbixLoginFeign.class, config.getUrl());

        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .method("user.login")
                .putParam("user", config.getUser())
                .putParam("password", config.getPassword())
                .build();

        return zabbixAPI.userLogin(request);
    }

}
