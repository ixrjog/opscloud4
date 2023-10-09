package com.baiyi.opscloud.datasource.aliyun.base;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/12/16 3:55 PM
 * @Version 1.0
 */
public class BaseAliyunTest extends BaseUnit {

    @Resource
    private DsConfigManager dsConfigManager;

    protected AliyunConfig getConfig() {
        return dsConfigManager.build(dsConfigManager.getConfigByDsType(DsTypeEnum.ALIYUN.getType()), AliyunConfig.class);
    }

    protected AliyunConfig getConfigById(int id) {
        return dsConfigManager.build(dsConfigManager.getConfigById(id), AliyunConfig.class);
    }

}