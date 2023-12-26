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

        @Schema(description = "警报")
        private String alertStatus;

        @Schema(description = "Bugs")
        private String bugs;

        @Schema(description = "异味")
        private String codeSmells;

        @Schema(description = "覆盖率%")
        private String coverage;

        @Schema(description = "重复行%")
        private String duplicatedLinesDensity;

        @Schema(description = "代码行数")
        private String ncloc;

        @Schema(description = "SQALE评级")
        private String sqaleRating;

        @Schema(description = "可靠性评级")
        private String reliabilityRating;

        @Schema(description = "安全评级")
        private String securityRating;

        @Schema(description = "技术债务")
        private String sqaleIndex;

        @Schema(description = "漏洞")
        private String vulnerabilities;

    }

}