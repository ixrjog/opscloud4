package com.baiyi.opscloud.datasource.nacos.base;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.NacosConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.service.datasource.DsConfigService;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/11/11 5:19 下午
 * @Version 1.0
 */
public class BaseNacosTest extends BaseUnit {

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsConfigManager dsFactory;

    protected NacosConfig getConfig() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(5);
        return dsFactory.build(datasourceConfig, NacosConfig.class);
    }
}
