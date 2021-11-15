package com.baiyi.opscloud.datasource.nacos.handler;

import com.baiyi.opscloud.common.datasource.NacosConfig;
import com.baiyi.opscloud.datasource.nacos.entry.NacosLogin;
import com.baiyi.opscloud.datasource.nacos.entry.NacosPermission;
import com.baiyi.opscloud.datasource.nacos.feign.NacosAuthV1Feign;
import com.baiyi.opscloud.datasource.nacos.param.NacosPageParam;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/11/12 3:49 下午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NacosPermissionHandler {

    private final NacosAuthHandler nacosAuthHandler;

    private NacosAuthV1Feign buildFeign(NacosConfig.Nacos config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(NacosAuthV1Feign.class, config.getUrl());
    }

    public NacosPermission.PermissionsResponse listPermissions(NacosConfig.Nacos config) {
        NacosLogin.AccessToken accessToken = nacosAuthHandler.login(config);
        NacosAuthV1Feign nacosAPI = buildFeign(config);
        NacosPageParam.PageQuery pageQuery = NacosPageParam.PageQuery.builder()
                .accessToken(accessToken.getAccessToken())
                .build();
        return nacosAPI.listPermissions(
                pageQuery.getPageNo(),
                pageQuery.getPageSize(),
                pageQuery.getAccessToken());
    }
}