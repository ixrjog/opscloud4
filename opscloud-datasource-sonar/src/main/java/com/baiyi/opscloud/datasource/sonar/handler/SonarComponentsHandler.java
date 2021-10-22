package com.baiyi.opscloud.datasource.sonar.handler;

import com.baiyi.opscloud.common.datasource.SonarDsInstanceConfig;
import com.baiyi.opscloud.datasource.sonar.entry.SonarComponents;
import com.baiyi.opscloud.datasource.sonar.feign.SonarComponentsFeign;
import com.baiyi.opscloud.datasource.sonar.param.SonarQubeRequestBuilder;
import com.google.common.base.Joiner;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/10/22 4:11 下午
 * @Version 1.0
 */
@Slf4j
@Component
@Deprecated
public class SonarComponentsHandler {

    private static final String QUALIFIERS = toQualifiers();

    private static String toQualifiers() {
        final String[] qualifiers = {"BRC", "DIR", "FIL", "TRK", "UTS"};
        return Joiner.on(",").join(qualifiers);
    }

    private Map<String, String> buildSearchComponentsParam(String queryName) {
        return SonarQubeRequestBuilder.newBuilder()
                .paramEntry("p", "1")
                .paramEntry("ps", "500")
                .paramEntry("q", queryName)
                .paramEntry("qualifiers", QUALIFIERS)
                .build().getParams();
    }

    public SonarComponents searchComponents(SonarDsInstanceConfig.Sonar config, String component) {
        SonarComponentsFeign sonarAPI = Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(SonarComponentsFeign.class, config.getUrl());

        return sonarAPI.searchComponents(config.getToken(), buildSearchComponentsParam(component));
    }
}
