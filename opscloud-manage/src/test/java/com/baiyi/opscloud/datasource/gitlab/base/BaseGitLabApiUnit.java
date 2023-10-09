package com.baiyi.opscloud.datasource.gitlab.base;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/10/26 13:59
 * @Version 1.0
 */
public class BaseGitLabApiUnit extends BaseUnit {

    @Resource
    private DsConfigManager dsConfigManager;

    protected GitLabConfig getConfigById(int id) {
        return dsConfigManager.build(dsConfigManager.getConfigById(id), GitLabConfig.class);
    }

    protected GitLabConfig getConfig() {
        return dsConfigManager.build(dsConfigManager.getConfigById(26), GitLabConfig.class);
    }

}
