package com.baiyi.opscloud.datasource.nexus.driver;

import com.baiyi.opscloud.common.datasource.NexusConfig;
import com.baiyi.opscloud.datasource.nexus.entity.NexusComponent;
import com.baiyi.opscloud.datasource.nexus.feign.NexusSearchV1Feign;
import com.baiyi.opscloud.datasource.nexus.param.SearchParam;
import com.baiyi.opscloud.domain.model.Authorization;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/4/23 15:50
 * @Version 1.0
 */
@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
@Slf4j
@Component
public class NexusSearchDriver {

    protected Authorization.Credential buildCredential(NexusConfig.Nexus config) {
        return Authorization.Credential.builder()
                .username(config.getUser())
                .password(config.getPassword())
                .build();
    }

    private NexusSearchV1Feign buildFeign(NexusConfig.Nexus config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(NexusSearchV1Feign.class, config.getUrl());
    }

    public NexusComponent.Components search(NexusConfig.Nexus config, SearchParam.SearchComponentsQuery query) {
        NexusSearchV1Feign nexusAPI = buildFeign(config);
        if (query.isContinuation()) {
            return nexusAPI.search(buildCredential(config).toBasic(),
                    query.getRepository(),
                    query.getGroup(),
                    query.getName(),
                    query.getVersion(),
                    query.getContinuationToken());
        } else {
            return nexusAPI.search(buildCredential(config).toBasic(),
                    query.getRepository(),
                    query.getGroup(),
                    query.getName(),
                    query.getVersion());
        }
    }

}