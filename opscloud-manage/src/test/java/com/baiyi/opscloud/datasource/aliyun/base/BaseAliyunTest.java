package com.baiyi.opscloud.datasource.aliyun.base;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/12/16 3:55 PM
 * @Version 1.0
 */
public class BaseAliyunTest extends BaseUnit {

    @Resource
    private DsConfigHelper dsConfigHelper;

    protected AliyunConfig getConfig() {
        return dsConfigHelper.build(dsConfigHelper.getConfigByDsType(DsTypeEnum.ALIYUN.getType()), AliyunConfig.class);
    }

}