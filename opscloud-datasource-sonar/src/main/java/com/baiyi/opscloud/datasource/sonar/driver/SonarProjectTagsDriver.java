package com.baiyi.opscloud.datasource.sonar.driver;

import com.baiyi.opscloud.common.datasource.SonarConfig;
import com.baiyi.opscloud.datasource.sonar.feign.SonarProjectTagsFeign;
import com.baiyi.opscloud.datasource.sonar.param.SonarQubeRequestBuilder;
import com.baiyi.opscloud.domain.model.Authorization;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/21 下午4:40
 * &#064;Version 1.0
 */
@Component
public class SonarProjectTagsDriver {

    private Map<String, String> buildSearchProjectsParam(String project, String tags) {
        return SonarQubeRequestBuilder.newBuilder()
                .putParam("project", project)
                .putParam("tags", tags)
                .build()
                .getParams();
    }

    public void setProjectTags(SonarConfig.Sonar config, String project, String tags) {
        SonarProjectTagsFeign sonarAPI = Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(SonarProjectTagsFeign.class, config.getUrl());
        Authorization.Token token = Authorization.Token.builder()
                .token(config.getToken())
                .build();
        sonarAPI.setProjectTags(token.toBasic(), buildSearchProjectsParam(project, tags));
    }

}
