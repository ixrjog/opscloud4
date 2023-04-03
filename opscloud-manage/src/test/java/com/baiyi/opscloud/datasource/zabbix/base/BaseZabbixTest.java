package com.baiyi.opscloud.datasource.zabbix.base;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/23 2:57 下午
 * @Version 1.0
 */
public class BaseZabbixTest extends BaseUnit {

    @Resource
    private DsConfigHelper dsConfigHelper;

    protected ZabbixConfig getConfig() {
        return dsConfigHelper.build(dsConfigHelper.getConfigByDsType(DsTypeEnum.ZABBIX.getType()), ZabbixConfig.class);
    }

}
