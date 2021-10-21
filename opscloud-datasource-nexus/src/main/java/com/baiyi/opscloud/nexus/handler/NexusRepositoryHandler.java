package com.baiyi.opscloud.nexus.handler;

import com.baiyi.opscloud.common.datasource.NexusDsInstanceConfig;
import com.baiyi.opscloud.nexus.entry.NexusRepository;
import com.baiyi.opscloud.nexus.feign.NexusRepositoriesV1Feign;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/10/15 1:45 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class NexusRepositoryHandler {

    private NexusRepositoriesV1Feign buildFeign(NexusDsInstanceConfig.Nexus config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(NexusRepositoriesV1Feign.class, config.getUrl());
    }

    public List<NexusRepository.Repository> list(NexusDsInstanceConfig.Nexus config) {
        NexusRepositoriesV1Feign nexusAPI = buildFeign(config);
        return nexusAPI.listRepositories();
    }

}
