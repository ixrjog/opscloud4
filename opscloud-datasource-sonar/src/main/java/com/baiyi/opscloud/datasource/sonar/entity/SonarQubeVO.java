package com.baiyi.opscloud.datasource.sonar.entity;

import com.baiyi.opscloud.datasource.sonar.entity.base.BaseSonarElement;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModel
    @Builder
    public static class SonarQube {

        private Map<String, BaseSonarElement.Measure> measures;

        // http://sonar.xinc818.com/dashboard?id=DATA-CENTER_data-center-server-dev
        private String projectUrl;

        @ApiModelProperty(value = "警报")
        private String alertStatus;

        @ApiModelProperty(value = "Bugs")
        private String bugs;

        @ApiModelProperty(value = "异味")
        private String codeSmells;

        @ApiModelProperty(value = "覆盖率%")
        private String coverage;

        @ApiModelProperty(value = "重复行%")
        private String duplicatedLinesDensity;

        @ApiModelProperty(value = "代码行数")
        private String ncloc;

        @ApiModelProperty(value = "SQALE评级")
        private String sqaleRating;

        @ApiModelProperty(value = "可靠性评级")
        private String reliabilityRating;

        @ApiModelProperty(value = "安全评级")
        private String securityRating;

        @ApiModelProperty(value = "技术债务")
        private String sqaleIndex;

        @ApiModelProperty(value = "漏洞")
        private String vulnerabilities;

    }
}
