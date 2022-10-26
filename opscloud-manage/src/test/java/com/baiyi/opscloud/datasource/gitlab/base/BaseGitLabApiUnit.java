package com.baiyi.opscloud.datasource.gitlab.base;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/10/26 13:59
 * @Version 1.0
 */
public class BaseGitLabApiUnit extends BaseUnit {

    @Resource
    private DsConfigHelper dsConfigHelper;

    protected GitlabConfig getConfigById(int id) {
        return dsConfigHelper.build(dsConfigHelper.getConfigById(id), GitlabConfig.class);
    }

    protected GitlabConfig getConfig() {
        // config_gitlab.chuanyinet.com
        return dsConfigHelper.build(dsConfigHelper.getConfigById(3), GitlabConfig.class);
    }

}
