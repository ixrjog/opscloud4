package com.baiyi.opscloud.datasource.jenkins.base;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/23 2:57 下午
 * @Version 1.0
 */
public class BaseJenkinsTest extends BaseUnit {

    @Resource
    private DsConfigManager dsConfigManager;

    protected JenkinsConfig getConfig() {
        return dsConfigManager.build(dsConfigManager.getConfigByDsType(DsTypeEnum.JENKINS.getType()), JenkinsConfig.class);
    }


    protected JenkinsConfig getConfigById(int id) {
        return dsConfigManager.build(dsConfigManager.getConfigById(id), JenkinsConfig.class);
    }

}
