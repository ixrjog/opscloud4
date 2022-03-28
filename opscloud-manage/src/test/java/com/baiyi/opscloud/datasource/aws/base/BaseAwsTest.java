package com.baiyi.opscloud.datasource.aws.base;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/1/21 11:15 AM
 * @Version 1.0
 */
public class BaseAwsTest extends BaseUnit {

    @Resource
    private DsConfigHelper dsConfigHelper;

    protected AwsConfig getConfig() {
        return dsConfigHelper.build(dsConfigHelper.getConfigByDsType(DsTypeEnum.AWS.getType()), AwsConfig.class);
    }

    protected AwsConfig getConfigById(Integer id) {
        return dsConfigHelper.build(dsConfigHelper.getConfigById(id), AwsConfig.class);
    }

}
