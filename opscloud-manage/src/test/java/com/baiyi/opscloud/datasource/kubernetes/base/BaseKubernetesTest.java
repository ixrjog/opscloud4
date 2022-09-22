package com.baiyi.opscloud.datasource.kubernetes.base;

import com.baiyi.opscloud.BaseUnit;
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

    public interface KubernetesClusterConfigs {
        int EKS_TEST = 24;
        int EKS_GRAY = 26;
        int EKS_PRE = 41;
        int EKS_PROD = 30;

        int ACK_DEV = 6;
        int ACK_DAILY = 10;
        int ACK_GRAY = 13;
        int ACK_PROD = 14;

    }

    protected KubernetesConfig getConfigById(int id) {
        return dsConfigHelper.build(dsConfigHelper.getConfigById(id), KubernetesConfig.class);
    }

    protected KubernetesConfig getConfig() {
        //return dsConfigHelper.build(dsConfigHelper.getConfigByDsType(DsTypeEnum.KUBERNETES.getType()), KubernetesConfig.class);
        return dsConfigHelper.build(dsConfigHelper.getConfigById(30), KubernetesConfig.class);
    }


}