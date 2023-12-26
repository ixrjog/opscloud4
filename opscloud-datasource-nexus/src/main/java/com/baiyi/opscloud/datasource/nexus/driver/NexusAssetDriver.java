package com.baiyi.opscloud.datasource.nexus.driver;

import com.baiyi.opscloud.common.datasource.NexusConfig;
import com.baiyi.opscloud.datasource.nexus.entity.NexusAsset;
import com.baiyi.opscloud.domain.model.Authorization;
import com.baiyi.opscloud.datasource.nexus.feign.NexusAssetsV1Feign;
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
@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
@Slf4j
@Component
public class NexusAssetDriver {

    protected Authorization.Credential buildCredential(NexusConfig.Nexus config) {
        return Authorization.Credential.builder()
                .username(config.getUser())
                .password(config.getPassword())
                .build();
    }

    private NexusAssetsV1Feign buildFeign(NexusConfig.Nexus config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(NexusAssetsV1Feign.class, config.getUrl());
    }

    public NexusAsset.Assets list(NexusConfig.Nexus config, String repository, String continuationToken) {
        NexusAssetsV1Feign nexusAPI = buildFeign(config);
        if (StringUtils.isBlank(continuationToken)) {
            return nexusAPI.listAssets(buildCredential(config).toBasic(), repository);
        } else {
            return nexusAPI.listAssets(buildCredential(config).toBasic(), repository, continuationToken);
        }
    }

}