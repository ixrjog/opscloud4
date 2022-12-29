package com.baiyi.opscloud.datasource.jenkins.base;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/23 2:57 下午
 * @Version 1.0
 */
public class BaseJenkinsTest extends BaseUnit {

    @Resource
    private DsConfigHelper dsConfigHelper;

    protected JenkinsConfig getConfig() {
        return dsConfigHelper.build(dsConfigHelper.getConfigByDsType(DsTypeEnum.JENKINS.getType()), JenkinsConfig.class);
    }


    protected JenkinsConfig getConfigById(int id) {
        return dsConfigHelper.build(dsConfigHelper.getConfigById(id), JenkinsConfig.class);
    }

}
