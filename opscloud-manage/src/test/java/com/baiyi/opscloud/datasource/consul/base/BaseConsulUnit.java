package com.baiyi.opscloud.datasource.consul.base;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.ConsulConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/7/26 09:39
 * @Version 1.0
 */
public class BaseConsulUnit extends BaseUnit {

    @Resource
    private DsConfigManager dsConfigManager;

    protected ConsulConfig getConfig() {
        return dsConfigManager.build(dsConfigManager.getConfigByDsType(DsTypeEnum.CONSUL.getType()), ConsulConfig.class);
    }

}