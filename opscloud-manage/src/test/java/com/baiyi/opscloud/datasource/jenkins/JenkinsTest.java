package com.baiyi.opscloud.datasource.jenkins;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.jenkins.base.BaseJenkinsTest;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsServerDriver;
import com.baiyi.opscloud.datasource.jenkins.engine.JenkinsBuildExecutorHelper;
import com.baiyi.opscloud.datasource.jenkins.model.*;
import com.baiyi.opscloud.datasource.jenkins.server.JenkinsServerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.datasource.jenkins.status.JenkinsBuildExecutorStatusVO;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/7/2 10:09 上午
 * @Version 1.0
 */
public class JenkinsTest extends BaseJenkinsTest {

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsConfigManager dsFactory;

    @Resource
    private DsInstanceService dsInstanceService;

    @Resource
    private JenkinsBuildExecutorHelper jenkinsEngineFacade;

    @Test
    void logTest() {
        JenkinsConfig jenkinsDsInstanceConfig = getConfig();

        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkinsDsInstanceConfig.getJenkins())) {
            Map<String, Computer> computerMap = jenkinsServer.getComputers();
            for (String s : computerMap.keySet()) {
                Computer c = computerMap.get(s);

                ComputerWithDetails computerWithDetails = c.details();
                System.err.print(computerWithDetails);
            }
            System.err.print(computerMap);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    void generatorEngineChartTest() {
        DatasourceInstance datasourceInstance = dsInstanceService.getById(18);
        JenkinsBuildExecutorStatusVO.Children children = jenkinsEngineFacade.generatorBuildExecutorStatus(datasourceInstance);
        print(children);
    }

    @Test
    void getJobsTest() {
        // 18 60
        JenkinsConfig config = getConfigById(60);
        try {
            Map<String, Job> jobMap = JenkinsServerDriver.getJobs(config.getJenkins());
            for (String s : jobMap.keySet()) {
                Job job = jobMap.get(s);
                print(job);
            }
        } catch (Exception e) {
            print(e.getMessage());
        }
    }

    @Test
    void getJobs2Test() {
        JenkinsConfig config = getConfigById(60);
        try {
            FolderJob folder = new FolderJob("templates", "https://leo-jenkins-1.chuanyinet.com/job/templates/");
            Map<String, Job> jobMap = JenkinsServerDriver.getJobs(config.getJenkins(), folder);
            for (String s : jobMap.keySet()) {
                Job job = jobMap.get(s);
                JobWithDetails jd = job.details();
                print(jd);
            }
        } catch (Exception e) {
            print(e.getMessage());
        }
    }

    @Test
    void getJobXmlTest() {
        JenkinsConfig config = getConfigById(60);
        try {
            // https://leo-jenkins-1.chuanyinet.com/job/templates/job/tpl_test/
            FolderJob folder = new FolderJob("templates", "https://leo-jenkins-1.chuanyinet.com/job/templates/");
            String xml = JenkinsServerDriver.getJobXml(config.getJenkins(), folder, "tpl_test");
            print(xml);

        } catch (Exception e) {
            print(e.getMessage());
        }
    }

}
