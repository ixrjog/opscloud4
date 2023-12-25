package com.baiyi.opscloud.datasource.metersphere.provider;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.MeterSphereConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.metersphere.entity.LeoBuildHook;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/5/15 14:11
 * @Version 1.0
 */
@Slf4j
@Component
public class MeterSphereBuildHookProvider extends BaseAssetProvider<LeoBuildHook> {

    @Resource
    private MeterSphereBuildHookProvider meterSphereBuildHookProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.METER_SPHERE.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.METER_SPHERE_BUILD_HOOK.name();
    }

    private MeterSphereConfig.MeterSphere buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, MeterSphereConfig.class).getMeterSphere();
    }

    @Override
    protected List<LeoBuildHook> listEntities(DsInstanceContext dsInstanceContext) {
        return Collections.emptyList();
    }

    @Override
    public void pullAsset(int dsInstanceId) {
       // TODO 不适用
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset a1, DatasourceInstanceAsset a2) {
      return false;
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(meterSphereBuildHookProvider);
    }

}