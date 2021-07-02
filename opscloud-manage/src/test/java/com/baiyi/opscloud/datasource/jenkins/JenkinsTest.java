package com.baiyi.opscloud.datasource.jenkins;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.JenkinsDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.base.BaseDsInstanceConfig;
import com.baiyi.opscloud.datasource.factory.DsConfigFactory;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.jenkins.handler.JenkinsServerHandler;
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
    private DsConfigFactory dsFactory;

    @Test
    void logTest() {
        JenkinsDsInstanceConfig jenkinsDsInstanceConfig = (JenkinsDsInstanceConfig) getConfig();
        try {
            Map<String, Computer> computerMap = JenkinsServerHandler.getComputers(jenkinsDsInstanceConfig.getJenkins());

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
    BaseDsInstanceConfig getConfig() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(6);
        return dsFactory.build(datasourceConfig, JenkinsDsInstanceConfig.class);
    }
}
