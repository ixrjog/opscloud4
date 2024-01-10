package com.baiyi.opscloud.datasource.sonar.driver;

import com.baiyi.opscloud.common.datasource.SonarConfig;
import com.baiyi.opscloud.datasource.sonar.entity.SonarProjects;
import com.baiyi.opscloud.datasource.sonar.enums.QualifierEnum;
import com.baiyi.opscloud.datasource.sonar.feign.SonarProjectsFeign;
import com.baiyi.opscloud.datasource.sonar.param.PagingParam;
import com.baiyi.opscloud.datasource.sonar.param.SonarQubeRequestBuilder;
import com.baiyi.opscloud.domain.model.Authorization;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/10/22 4:33 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class SonarProjectsDriver {

    private static final QualifierEnum[] QUALIFIERS = {QualifierEnum.TRK, QualifierEnum.APP, QualifierEnum.VW};

    private Map<String, String> buildSearchProjectsParam(PagingParam pagingParam) {
        return SonarQubeRequestBuilder.newBuilder()
                .putParam(pagingParam)
                .putParam(QUALIFIERS)
                .build()
                .getParams();
    }

    public SonarProjects searchProjects(SonarConfig.Sonar config, PagingParam pagingParam) {
        SonarProjectsFeign sonarAPI = Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(SonarProjectsFeign.class, config.getUrl());
        Authorization.Token token = Authorization.Token.builder()
                .token(config.getToken())
                .build();
        return sonarAPI.searchProjects(token.toBasic(), buildSearchProjectsParam(pagingParam));
    }

}