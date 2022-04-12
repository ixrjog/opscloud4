package com.baiyi.opscloud.datasource.jenkins;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.jenkins.base.BaseJenkinsTest;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsServerDriver;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.datasource.jenkins.model.Computer;
import com.baiyi.opscloud.datasource.jenkins.model.ComputerWithDetails;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
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
    private DsConfigHelper dsFactory;

    @Test
    void logTest() {
        JenkinsConfig jenkinsDsInstanceConfig = getConfig() ;
        try {
            Map<String, Computer> computerMap = JenkinsServerDriver.getComputers(jenkinsDsInstanceConfig.getJenkins());

            for (String s : computerMap.keySet()) {
                Computer c = computerMap.get(s);

                ComputerWithDetails  computerWithDetails = c.details();
                System.err.print(computerWithDetails);
            }

            System.err.print(computerMap);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


//    @Test
//    BaseConfig getConfig() {
//        DatasourceConfig datasourceConfig = dsConfigService.getById(6);
//        return dsFactory.build(datasourceConfig, JenkinsConfig.class);
//    }
}
