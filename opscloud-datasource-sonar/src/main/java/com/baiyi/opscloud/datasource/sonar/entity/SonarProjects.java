package com.baiyi.opscloud.datasource.sonar.entity;

import com.baiyi.opscloud.datasource.sonar.entity.base.BaseSonarElement;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/10/22 4:33 下午
 * @Version 1.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SonarProjects {

    private BaseSonarElement.Paging paging;
    private List<BaseSonarElement.Project> components;

}