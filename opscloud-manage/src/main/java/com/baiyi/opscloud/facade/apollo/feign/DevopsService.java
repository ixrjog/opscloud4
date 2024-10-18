package com.baiyi.opscloud.facade.apollo.feign;

import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/8 10:50
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DevopsService {

    private static final String DEVOPS_URL = "https://devops.palmpay-inc.com";

    private DevopsFeign buildFeign() {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(DevopsFeign.class, DEVOPS_URL);
    }

    public void callHook(ApolloParam.ReleaseEvent releaseEvent, ApolloConfig apolloConfig) {
        try {
            releaseEvent.setUrl(apolloConfig.getUrl());
            DevopsFeign devopsAPI = buildFeign();
            devopsAPI.callHook(releaseEvent);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}