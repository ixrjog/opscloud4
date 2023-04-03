package com.baiyi.opscloud.datasource.sonar.entity;

import com.baiyi.opscloud.datasource.sonar.entity.base.BaseSonarElement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/3/17 3:26 下午
 * @Version 1.0
 */
public class SonarQubeVO {

    public interface ISonarQube {
        Integer getApplicationId();

        String getJobKey();

        Boolean enableSonar();

        void setSonarQube(SonarQube sonarQube);
    }

    @Data
    @Schema
    @Builder
    public static class SonarQube {

        private Map<String, BaseSonarElement.Measure> measures;

        // http://sonar.xinc818.com/dashboard?id=DATA-CENTER_data-center-server-dev
        private String projectUrl;

        @Schema(name = "警报")
        private String alertStatus;

        @Schema(name = "Bugs")
        private String bugs;

        @Schema(name = "异味")
        private String codeSmells;

        @Schema(name = "覆盖率%")
        private String coverage;

        @Schema(name = "重复行%")
        private String duplicatedLinesDensity;

        @Schema(name = "代码行数")
        private String ncloc;

        @Schema(name = "SQALE评级")
        private String sqaleRating;

        @Schema(name = "可靠性评级")
        private String reliabilityRating;

        @Schema(name = "安全评级")
        private String securityRating;

        @Schema(name = "技术债务")
        private String sqaleIndex;

        @Schema(name = "漏洞")
        private String vulnerabilities;

    }
}
