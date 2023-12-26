package com.baiyi.opscloud.datasource.sonar.entity;


import com.baiyi.opscloud.datasource.sonar.entity.base.BaseSonarElement;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/12/24 3:54 下午
 * @Version 1.0
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SonarMeasures {

    private BaseSonarElement.Component component;

}