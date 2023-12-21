package com.baiyi.opscloud.facade.kubernetes.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2023/10/18 16:46
 * @Version 1.0
 */
public abstract class BaseKubernetesConfig {

    @Resource
    private DsInstanceService dsInstanceService;

    @Resource
    private DsConfigManager dsConfigManager;

    protected KubernetesConfig getKubernetesConfig(int instanceId) {
        DatasourceInstance datasourceInstance = dsInstanceService.getById(instanceId);
        if (datasourceInstance == null) {
            throw new OCException("数据源实例不存在: instanceId={}", instanceId);
        }
        if (!DsTypeEnum.KUBERNETES.name().equals(datasourceInstance.getInstanceType())) {
            throw new OCException("数据源实例类型不正确: instanceType={}", datasourceInstance.getInstanceType());
        }
        return dsConfigManager.buildKubernetesConfig(datasourceInstance.getUuid());
    }

}