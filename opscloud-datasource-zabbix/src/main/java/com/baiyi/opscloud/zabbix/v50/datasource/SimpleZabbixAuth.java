package com.baiyi.opscloud.zabbix.v50.datasource;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.zabbix.v50.feign.ZabbixLoginFeign;
import com.baiyi.opscloud.zabbix.v50.request.ZabbixRequest;
import com.baiyi.opscloud.zabbix.v50.request.builder.ZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.v50.response.LoginResponse;
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

    public static final String AUTH_CACHE_KAY_PREFIX = "opscloud_v4_zabbix_auth";

    private final RedisUtil redisUtil;

    private String buildKey(ZabbixConfig.Zabbix config) {
        return Joiner.on("_").join(AUTH_CACHE_KAY_PREFIX, config.getUrl());
    }

    public String getAuth(ZabbixConfig.Zabbix config) {
        String key = buildKey(config);
        if (redisUtil.hasKey(key)) {
            return (String) redisUtil.get(key);
        } else {
            LoginResponse.LoginAuth loginAuth = login(config);
            redisUtil.set(key, loginAuth.getResult(), 300);
            return loginAuth.getResult();
        }
    }

    private LoginResponse.LoginAuth login(ZabbixConfig.Zabbix config) {
        ZabbixLoginFeign zabbixAPI = Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(ZabbixLoginFeign.class, config.getUrl());

        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .setMethod("user.login")
                .putParam("user", config.getUser())
                .putParam("password", config.getPassword())
                .build();

        return zabbixAPI.userLogin(request);
    }

}
