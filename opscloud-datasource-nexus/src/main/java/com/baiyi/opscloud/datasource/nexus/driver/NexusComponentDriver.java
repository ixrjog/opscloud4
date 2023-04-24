package com.baiyi.opscloud.datasource.nexus.driver;

import com.baiyi.opscloud.common.datasource.NexusConfig;
import com.baiyi.opscloud.datasource.nexus.entity.NexusComponent;
import com.baiyi.opscloud.datasource.nexus.feign.NexusComponentsV1Feign;
import com.baiyi.opscloud.domain.model.Authorization;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/4/23 11:16
 * @Version 1.0
 */
@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
@Slf4j
@Component
public class NexusComponentDriver {

    protected Authorization.Credential buildCredential(NexusConfig.Nexus config) {
        return Authorization.Credential.builder()
                .username(config.getUser())
                .password(config.getPassword())
                .build();
    }

    private NexusComponentsV1Feign buildFeign(NexusConfig.Nexus config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(NexusComponentsV1Feign.class, config.getUrl());
    }

    public NexusComponent.Component get(NexusConfig.Nexus config, String id) {
        NexusComponentsV1Feign nexusAPI = buildFeign(config);
        return nexusAPI.getComponent(buildCredential(config).toBasic(), id);
    }

}