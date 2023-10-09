package com.baiyi.opscloud.datasource.aws.base;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/1/21 11:15 AM
 * @Version 1.0
 */
public class BaseAwsTest extends BaseUnit {

    protected static final String DEFAULT_DSINSTANCE_UUID = "9877af2fa97f48faa34608531df354d2";

    protected static final String DEFAULT_REGION_ID = "eu-west-1";

    @Resource
    private DsConfigManager dsConfigManager;

    @Resource
    protected DsInstanceAssetService dsInstanceAssetService;

    protected AwsConfig getConfig() {
        return dsConfigManager.build(dsConfigManager.getConfigByDsType(DsTypeEnum.AWS.getType()), AwsConfig.class);
    }

    protected AwsConfig getConfigById(Integer id) {
        return dsConfigManager.build(dsConfigManager.getConfigById(id), AwsConfig.class);
    }

}
