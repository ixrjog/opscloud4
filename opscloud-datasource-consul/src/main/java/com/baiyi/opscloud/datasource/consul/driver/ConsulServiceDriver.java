package com.baiyi.opscloud.datasource.consul.driver;

import com.baiyi.opscloud.common.datasource.ConsulConfig;
import com.baiyi.opscloud.datasource.consul.entity.ConsulHealth;
import com.baiyi.opscloud.datasource.consul.entity.ConsulService;
import com.baiyi.opscloud.datasource.consul.feign.ConsulServiceV1Feign;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/7/14 14:24
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConsulServiceDriver {

    private ConsulServiceV1Feign buildFeign(ConsulConfig.Consul config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 2))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(ConsulServiceV1Feign.class, config.getUrl());
    }

    public List<ConsulService.Service> listServices(ConsulConfig.Consul config, String dataCenter) {
        ConsulServiceV1Feign consulAPI = buildFeign(config);
        return consulAPI.listServices(dataCenter);
    }

    public List<ConsulHealth.Health> listHealthService(ConsulConfig.Consul config, String service, String dataCenter) {
        ConsulServiceV1Feign consulAPI = buildFeign(config);
        return consulAPI.listHealthService(service, dataCenter);
    }

}
