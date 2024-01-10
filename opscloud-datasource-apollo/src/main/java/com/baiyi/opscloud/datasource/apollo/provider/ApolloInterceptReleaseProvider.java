package com.baiyi.opscloud.datasource.apollo.provider;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.MeterSphereConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.apollo.entity.InterceptRelease;
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
 * @Date 2023/6/13 15:14
 * @Version 1.0
 */
@Slf4j
@Component
public class ApolloInterceptReleaseProvider extends BaseAssetProvider<InterceptRelease.Event> {

    @Resource
    private ApolloInterceptReleaseProvider apolloInterceptReleaseProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.APOLLO.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.APOLLO_INTERCEPT_RELEASE.name();
    }

    private MeterSphereConfig.MeterSphere buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, MeterSphereConfig.class).getMeterSphere();
    }

    @Override
    protected List<InterceptRelease.Event> listEntities(DsInstanceContext dsInstanceContext) {
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
        AssetProviderFactory.register(apolloInterceptReleaseProvider);
    }

}
