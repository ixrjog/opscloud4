package com.baiyi.opscloud.datasource.jenkins;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.common.datasource.base.BaseConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.datasource.jenkins.drive.JenkinsServerDrive;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.offbytwo.jenkins.model.Computer;
import com.offbytwo.jenkins.model.ComputerWithDetails;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/7/2 10:09 上午
 * @Version 1.0
 */
public class JenkinsTest extends BaseUnit {

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsConfigHelper dsFactory;

    @Test
    void logTest() {
        JenkinsConfig jenkinsDsInstanceConfig = (JenkinsConfig) getConfig();
        try {
            Map<String, Computer> computerMap = JenkinsServerDrive.getComputers(jenkinsDsInstanceConfig.getJenkins());

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


    @Test
    BaseConfig getConfig() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(6);
        return dsFactory.build(datasourceConfig, JenkinsConfig.class);
    }
}
