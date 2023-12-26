package com.baiyi.opscloud.datasource.sonar.driver;

import com.baiyi.opscloud.common.datasource.SonarConfig;
import com.baiyi.opscloud.datasource.sonar.entity.SonarMeasures;
import com.baiyi.opscloud.datasource.sonar.feign.SonarMeasuresFeign;
import com.baiyi.opscloud.datasource.sonar.param.SonarQubeRequestBuilder;
import com.baiyi.opscloud.domain.model.Authorization;
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
 * @Date 2021/10/22 2:51 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class SonarMeasuresDriver {

    private static final String METRIC_KEYS = toMetricKeys();

    private Map<String, String> buildMeasuresComponentParam(String component) {
        return SonarQubeRequestBuilder.newBuilder()
                .putParam("additionalFields", "metrics,periods")
                .putParam("component", component)
                .putParam("metricKeys", METRIC_KEYS)
                .build().getParams();
    }

    private static String toMetricKeys() {
        final String[] keys = {"alert_status",
                "quality_gate_details", "bugs", "new_bugs", "reliability_rating",
                "new_reliability_rating", "vulnerabilities", "new_vulnerabilities", "security_rating",
                "new_security_rating", "security_hotspots", "new_security_hotspots", "code_smells",
                "new_code_smells", "sqale_rating", "new_maintainability_rating",
                "sqale_index", "new_technical_debt", "coverage", "new_coverage", "new_lines_to_cover",
                "tests", "duplicated_lines_density", "new_duplicated_lines_density",
                "duplicated_blocks", "ncloc", "ncloc_language_distribution", "projects", "new_lines"};
        return Joiner.on(",").join(keys);
    }

    public SonarMeasures queryMeasuresComponent(SonarConfig.Sonar config, String component) {
        SonarMeasuresFeign sonarAPI = Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(SonarMeasuresFeign.class, config.getUrl());
        Authorization.Token token = Authorization.Token.builder()
                .token(config.getToken())
                .build();
        return sonarAPI.queryMeasuresComponent(token.toBasic(), buildMeasuresComponentParam(component));
    }

}