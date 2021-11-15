package com.baiyi.opscloud.datasource.nacos.handler;

import com.baiyi.opscloud.common.datasource.NacosConfig;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.datasource.nacos.entry.NacosLogin;
import com.baiyi.opscloud.datasource.nacos.entry.NacosPermission;
import com.baiyi.opscloud.datasource.nacos.entry.NacosRole;
import com.baiyi.opscloud.datasource.nacos.entry.NacosUser;
import com.baiyi.opscloud.datasource.nacos.feign.NacosAuthV1Feign;
import com.baiyi.opscloud.datasource.nacos.param.NacosPageParam;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2021/11/11 4:55 下午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NacosAuthHandler {

    private final RedisUtil redisUtil;

    private String buildKey(String url) {
        return String.format("Opscloud.V4.Nacos.AccessToken.%s", url);
    }

    private NacosAuthV1Feign buildFeign(NacosConfig.Nacos config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(NacosAuthV1Feign.class, config.getUrl());
    }

    public NacosLogin.AccessToken login(NacosConfig.Nacos config) {
        String key = buildKey(config.getUrl());
        if (redisUtil.hasKey(key)) {
            return (NacosLogin.AccessToken) redisUtil.get(key);
        }
        NacosAuthV1Feign nacosAPI = buildFeign(config);
        NacosLogin.AccessToken accessToken = nacosAPI.login(config.getLoginParam());
        redisUtil.set(key, accessToken, accessToken.getTokenTtl());
        return accessToken;
    }

    public NacosPermission.PermissionsResponse listPermissions(NacosConfig.Nacos config,NacosPageParam.PageQuery pageQuery) {
        preHandle(config,pageQuery);
        NacosAuthV1Feign nacosAPI = buildFeign(config);
        return nacosAPI.listPermissions(
                pageQuery.getPageNo(),
                pageQuery.getPageSize(),
                pageQuery.getAccessToken());
    }

    public NacosUser.UsersResponse listUsers(NacosConfig.Nacos config, NacosPageParam.PageQuery pageQuery) {
        preHandle(config,pageQuery);
        NacosAuthV1Feign nacosAPI = buildFeign(config);
        return nacosAPI.listUsers(
                pageQuery.getPageNo(),
                pageQuery.getPageSize(),
                pageQuery.getAccessToken());
    }

    public NacosRole.RolesResponse listRoles(NacosConfig.Nacos config, NacosPageParam.PageQuery pageQuery) {
        preHandle(config,pageQuery);
        NacosAuthV1Feign nacosAPI = buildFeign(config);
        return nacosAPI.listRoles(
                pageQuery.getPageNo(),
                pageQuery.getPageSize(),
                pageQuery.getAccessToken());
    }

    private void preHandle(NacosConfig.Nacos config,NacosPageParam.PageQuery pageQuery){
        if(StringUtils.isEmpty(pageQuery.getAccessToken())){
            NacosLogin.AccessToken accessToken = this.login(config);
            pageQuery.setAccessToken(accessToken.getAccessToken());
        }
    }

}
