package com.baiyi.opscloud.datasource.dingtalk.driver;

import com.baiyi.opscloud.common.datasource.DingtalkConfig;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkToken;
import com.baiyi.opscloud.datasource.dingtalk.feign.DingtalkTokenFeign;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/11/29 3:22 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class DingtalkTokenDriver {

    private final RedisUtil redisUtil;

    private String buildKey(String name) {
        return StringFormatter.format("Opscloud.V4.Dingtalk.Token.{}", name);
    }

    private DingtalkTokenFeign buildFeign(DingtalkConfig.Dingtalk config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(DingtalkTokenFeign.class, config.getUrl());
    }

    public DingtalkToken.TokenResponse getToken(DingtalkConfig.Dingtalk config) {
        String key = buildKey(config.getApp().getName());
        if (redisUtil.hasKey(key)) {
            return (DingtalkToken.TokenResponse) redisUtil.get(key);
        }
        DingtalkTokenFeign dingtalkAPI = buildFeign(config);
        DingtalkToken.TokenResponse token = dingtalkAPI.getToken(config.getApp().getAppKey(), config.getApp().getAppSecret());
        redisUtil.set(key, token, token.getExpiresIn());
        return token;
    }

}