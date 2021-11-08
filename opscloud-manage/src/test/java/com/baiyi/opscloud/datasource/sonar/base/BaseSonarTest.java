package com.baiyi.opscloud.datasource.sonar.base;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.SonarDsInstanceConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.service.datasource.DsConfigService;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/10/22 3:11 下午
 * @Version 1.0
 */
public class BaseSonarTest extends BaseUnit {

        @Resource
        private DsConfigService dsConfigService;

        @Resource
        private DsConfigHelper dsFactory;

        protected SonarDsInstanceConfig getConfig() {
            DatasourceConfig datasourceConfig = dsConfigService.getById(15);
            return dsFactory.build(datasourceConfig, SonarDsInstanceConfig.class);
        }
}
