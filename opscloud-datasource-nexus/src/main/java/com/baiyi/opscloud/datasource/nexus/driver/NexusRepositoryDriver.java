package com.baiyi.opscloud.datasource.nexus.driver;

import com.baiyi.opscloud.common.datasource.NexusConfig;
import com.baiyi.opscloud.datasource.nexus.entity.NexusRepository;
import com.baiyi.opscloud.datasource.nexus.feign.NexusRepositoriesV1Feign;
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
@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
@Slf4j
@Component
public class NexusRepositoryDriver {

    private NexusRepositoriesV1Feign buildFeign(NexusConfig.Nexus config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(NexusRepositoriesV1Feign.class, config.getUrl());
    }

    public List<NexusRepository.Repository> list(NexusConfig.Nexus config) {
        NexusRepositoriesV1Feign nexusAPI = buildFeign(config);
        return nexusAPI.listRepositories();
    }

}