package com.baiyi.opscloud.datasource.huaweicloud.base;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.HuaweicloudConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/7/7 15:44
 * @Version 1.0
 */
public class BaseHuaweicloudTest extends BaseUnit {

    @Resource
    private DsConfigHelper dsConfigHelper;

    protected HuaweicloudConfig getConfig() {
        return dsConfigHelper.build(dsConfigHelper.getConfigByDsType(DsTypeEnum.HUAWEICLOUD.getType()), HuaweicloudConfig.class);
    }

}