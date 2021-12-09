package com.baiyi.opscloud.datasource.kubernetes.base;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/11/24 3:07 下午
 * @Version 1.0
 */
public class BaseKubernetesTest extends BaseUnit {

    @Resource
    private DsConfigHelper dsConfigHelper;

    protected KubernetesConfig getConfig() {
        return dsConfigHelper.build(dsConfigHelper.getConfigByDsType(DsTypeEnum.KUBERNETES.getType()), KubernetesConfig.class);
    }

}