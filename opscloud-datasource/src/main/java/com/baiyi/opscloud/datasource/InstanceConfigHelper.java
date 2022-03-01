package com.baiyi.opscloud.datasource;

import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.common.datasource.base.BaseConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/10/29 10:23 上午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class InstanceConfigHelper {

    private final DsConfigService dsConfigService;

    private final DsConfigHelper dsConfigHelper;

    public BaseConfig getConfig(DatasourceInstance datasourceInstance) {
        DatasourceConfig datasourceConfig = dsConfigService.getById(datasourceInstance.getConfigId());
        return dsConfigHelper.build(datasourceConfig, GitlabConfig.class);
    }

}
