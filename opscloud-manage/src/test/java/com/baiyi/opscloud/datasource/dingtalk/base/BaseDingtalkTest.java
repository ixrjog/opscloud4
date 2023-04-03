package com.baiyi.opscloud.datasource.dingtalk.base;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.DingtalkConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/11/29 3:36 下午
 * @Version 1.0
 */
public class BaseDingtalkTest extends BaseUnit {

    @Resource
    private DsConfigHelper dsConfigHelper;

    protected DingtalkConfig getConfig() {
        return dsConfigHelper.build(dsConfigHelper.getConfigByDsType(DsTypeEnum.DINGTALK_APP.getType()), DingtalkConfig.class);
    }
}
