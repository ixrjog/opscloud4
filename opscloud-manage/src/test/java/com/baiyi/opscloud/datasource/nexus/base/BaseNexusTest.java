package com.baiyi.opscloud.datasource.nexus.base;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.NexusConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.service.datasource.DsConfigService;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/10/15 1:58 下午
 * @Version 1.0
 */
public class BaseNexusTest extends BaseUnit {

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsConfigManager dsFactory;

    protected NexusConfig getConfig() {
        // nexus.transspay.net
        DatasourceConfig datasourceConfig = dsConfigService.getById(75);
        return dsFactory.build(datasourceConfig, NexusConfig.class);
    }
}
