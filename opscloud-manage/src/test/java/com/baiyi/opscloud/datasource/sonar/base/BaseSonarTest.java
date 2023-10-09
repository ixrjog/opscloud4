package com.baiyi.opscloud.datasource.sonar.base;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.SonarConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.service.datasource.DsConfigService;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/10/22 3:11 下午
 * @Version 1.0
 */
public class BaseSonarTest extends BaseUnit {

        @Resource
        private DsConfigService dsConfigService;

        @Resource
        private DsConfigManager dsFactory;

        protected SonarConfig getConfig() {
            DatasourceConfig datasourceConfig = dsConfigService.getById(15);
            return dsFactory.build(datasourceConfig, SonarConfig.class);
        }
}
