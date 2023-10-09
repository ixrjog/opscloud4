package com.baiyi.opscloud.datasource.kubernetes.base;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/11/24 3:07 下午
 * @Version 1.0
 */
public class BaseKubernetesTest extends BaseUnit {

    @Resource
    private DsConfigManager dsConfigManager;

    public interface KubernetesClusterConfigs {
        int EKS_TEST = 24;
        int EKS_GRAY = 26;
        int EKS_PRE = 41;
        int EKS_PROD = 30;

        int ACK_DEV = 6;
        int ACK_DAILY = 10;
        int ACK_GRAY = 13;
        int ACK_PRE = 13;
        int ACK_PROD = 14;

        int ACK_FRANKFURT_DEV = 39;
        int ACK_FRANKFURT_DAILY = 40;
        int ACK_FRANKFURT_SIT = 86;
        int ACK_FRANKFURT_PRE = 44;
        int ACK_FRANKFURT_PROD = 48;

        int ACK_FE = 83;
    }

    protected KubernetesConfig getConfigById(int id) {
        return dsConfigManager.build(dsConfigManager.getConfigById(id), KubernetesConfig.class);
    }

    protected KubernetesConfig getConfig() {
        return dsConfigManager.build(dsConfigManager.getConfigById(30), KubernetesConfig.class);
    }

}