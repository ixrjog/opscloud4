package com.baiyi.opscloud.datasource.nacos.driver;

import com.baiyi.opscloud.common.datasource.NacosConfig;
import com.baiyi.opscloud.datasource.nacos.entity.NacosCluster;
import com.baiyi.opscloud.datasource.nacos.entity.NacosLogin;
import com.baiyi.opscloud.datasource.nacos.feign.NacosClusterV1Feign;
import com.baiyi.opscloud.datasource.nacos.param.NacosClusterParam;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/11/12 10:26 上午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NacosClusterDriver {

    private final NacosAuthDriver nacosAuthDatasource;

    private NacosClusterV1Feign buildFeign(NacosConfig.Nacos config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(NacosClusterV1Feign.class, config.getUrl());
    }

    public NacosCluster.NodesResponse listNodes(NacosConfig.Nacos config) {
        NacosLogin.AccessToken accessToken = nacosAuthDatasource.login(config);
        NacosClusterV1Feign nacosAPI = buildFeign(config);
        NacosClusterParam.NodesQuery queryParam = NacosClusterParam.NodesQuery.builder()
                .accessToken(accessToken.getAccessToken())
                .build();
        return nacosAPI.listNodes(
                queryParam.getWithInstances(),
                queryParam.getPageNo(),
                queryParam.getPageSize(),
                queryParam.getKeyword(),
                queryParam.getNamespaceId(),
                queryParam.getAccessToken()
        );
    }

}