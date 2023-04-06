package com.baiyi.opscloud.datasource.sonar;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.datasource.sonar.base.BaseSonarTest;
import com.baiyi.opscloud.datasource.sonar.entity.SonarComponents;
import com.baiyi.opscloud.datasource.sonar.entity.SonarMeasures;
import com.baiyi.opscloud.datasource.sonar.entity.SonarProjects;
import com.baiyi.opscloud.datasource.sonar.driver.SonarComponentsDriver;
import com.baiyi.opscloud.datasource.sonar.driver.SonarMeasuresDriver;
import com.baiyi.opscloud.datasource.sonar.driver.SonarProjectsDriver;
import com.baiyi.opscloud.datasource.sonar.param.PagingParam;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/10/22 3:11 下午
 * @Version 1.0
 */
public class SonarTest extends BaseSonarTest {

    @Resource
    private SonarMeasuresDriver sonarMeasuresHandler;

    @Resource
    private SonarComponentsDriver sonarComponentsHandler;

    @Resource
    private SonarProjectsDriver sonarProjectsHandler;

    @Test
    void searchProjectsTest() {
        PagingParam pagingParam = PagingParam.builder()
                .build();
        SonarProjects sps = sonarProjectsHandler.searchProjects(getConfig().getSonar(), pagingParam);
        System.err.println(JSONUtil.writeValueAsString(sps));
    }

    @Test
    void queryMeasuresComponentTest() {
        final String component = ""; // "ACCOUNT_account-server-daily"
        SonarMeasures sonarMeasures = sonarMeasuresHandler.queryMeasuresComponent(getConfig().getSonar(), component);
        System.err.println(JSONUtil.writeValueAsString(sonarMeasures));
    }

    @Test
    void searchComponentsTest() {
        final String queryName = "";
        SonarComponents sc = sonarComponentsHandler.searchComponents(getConfig().getSonar(), queryName);
        System.err.println(JSONUtil.writeValueAsString(sc));
    }

}
