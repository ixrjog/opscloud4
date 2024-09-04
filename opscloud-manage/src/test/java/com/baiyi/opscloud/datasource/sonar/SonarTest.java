package com.baiyi.opscloud.datasource.sonar;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.datasource.sonar.base.BaseSonarTest;
import com.baiyi.opscloud.datasource.sonar.driver.SonarComponentsDriver;
import com.baiyi.opscloud.datasource.sonar.driver.SonarMeasuresDriver;
import com.baiyi.opscloud.datasource.sonar.driver.SonarProjectTagsDriver;
import com.baiyi.opscloud.datasource.sonar.driver.SonarProjectsDriver;
import com.baiyi.opscloud.datasource.sonar.entity.SonarComponents;
import com.baiyi.opscloud.datasource.sonar.entity.SonarMeasures;
import com.baiyi.opscloud.datasource.sonar.entity.SonarProjects;
import com.baiyi.opscloud.datasource.sonar.param.PagingParam;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2021/10/22 3:11 下午
 * @Version 1.0
 */
public class SonarTest extends BaseSonarTest {

    @Resource
    private SonarMeasuresDriver sonarMeasuresDriver;

    @Resource
    private SonarComponentsDriver sonarComponentsDriver;

    @Resource
    private SonarProjectsDriver sonarProjectsDriver;

    @Resource
    private SonarProjectTagsDriver sonarProjectTagsDriver;


    @Test
    void searchProjectsTest() {
        PagingParam pagingParam = PagingParam.builder()
                .build();
        SonarProjects sps = sonarProjectsDriver.searchProjects(getConfig().getSonar(), pagingParam);
        System.err.println(JSONUtil.writeValueAsString(sps));
    }

    @Test
    void queryMeasuresComponentTest() {
        final String component = ""; // "ACCOUNT_account-server-daily"
        SonarMeasures sonarMeasures = sonarMeasuresDriver.queryMeasuresComponent(getConfig().getSonar(), component);
        System.err.println(JSONUtil.writeValueAsString(sonarMeasures));
    }

    @Test
    void searchComponentsTest() {
        final String queryName = "";
        SonarComponents sc = sonarComponentsDriver.searchComponents(getConfig().getSonar(), queryName);
        System.err.println(JSONUtil.writeValueAsString(sc));
    }

    @Test
    void sonarProjectTagsDriverTest() {
        sonarProjectTagsDriver.setProjectTags(getConfig().getSonar(), "CRATOS", "a88");
    }

}
