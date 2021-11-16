package com.baiyi.opscloud.datasource.sonar;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.datasource.sonar.base.BaseSonarTest;
import com.baiyi.opscloud.datasource.sonar.entry.SonarComponents;
import com.baiyi.opscloud.datasource.sonar.entry.SonarMeasures;
import com.baiyi.opscloud.datasource.sonar.entry.SonarProjects;
import com.baiyi.opscloud.datasource.sonar.datasource.SonarComponentsDatasource;
import com.baiyi.opscloud.datasource.sonar.datasource.SonarMeasuresDatasource;
import com.baiyi.opscloud.datasource.sonar.datasource.SonarProjectsDatasource;
import com.baiyi.opscloud.datasource.sonar.param.PagingParam;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/10/22 3:11 下午
 * @Version 1.0
 */
public class SonarTest extends BaseSonarTest {

    @Resource
    private SonarMeasuresDatasource sonarMeasuresHandler;

    @Resource
    private SonarComponentsDatasource sonarComponentsHandler;

    @Resource
    private SonarProjectsDatasource sonarProjectsHandler;

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
