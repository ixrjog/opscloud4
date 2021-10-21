package com.baiyi.opscloud.nexus.handler;

import com.baiyi.opscloud.common.datasource.NexusDsInstanceConfig;
import com.baiyi.opscloud.domain.model.Authorization;
import com.baiyi.opscloud.nexus.entry.NexusAsset;
import com.baiyi.opscloud.nexus.feign.NexusAssetsV1Feign;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/10/15 3:10 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class NexusAssetHandler {

    protected Authorization.Credential buildCredential(NexusDsInstanceConfig.Nexus config) {
        return Authorization.Credential.builder()
                .username(config.getUser())
                .password(config.getPassword())
                .build();
    }

    private NexusAssetsV1Feign buildFeign(NexusDsInstanceConfig.Nexus config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(NexusAssetsV1Feign.class, config.getUrl());
    }

    public NexusAsset.Assets list(NexusDsInstanceConfig.Nexus config, String repository, String continuationToken) {
        NexusAssetsV1Feign nexusAPI = buildFeign(config);
        if (StringUtils.isBlank(continuationToken)) {
            return nexusAPI.listAssets(buildCredential(config).toBasic(), repository);
        } else {
            return nexusAPI.listAssets(buildCredential(config).toBasic(), repository, continuationToken);
        }
    }
}
