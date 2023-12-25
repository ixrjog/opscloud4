package com.baiyi.opscloud.datasource.apollo.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.apollo.converter.AppConverter;
import com.baiyi.opscloud.datasource.apollo.driver.ApolloAppDriver;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.ctrip.framework.apollo.openapi.dto.OpenAppDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_APOLLO_APP;

/**
 * @Author baiyi
 * @Date 2023/5/30 10:01
 * @Version 1.0
 */
@Slf4j
@Component
public class ApolloAppProvider extends BaseAssetProvider<OpenAppDTO> {

    @Resource
    private ApolloAppProvider apolloAppProvider;


    @Override
    public String getInstanceType() {
        return DsTypeEnum.APOLLO.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.APOLLO_APP.name();
    }

    private ApolloConfig.Apollo buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, ApolloConfig.class).getApollo();
    }

    @Override
    protected List<OpenAppDTO> listEntities(DsInstanceContext dsInstanceContext) {
        ApolloConfig.Apollo apollo = buildConfig(dsInstanceContext.getDsConfig());
        return ApolloAppDriver.getAllApps(apollo);
    }

    @Override
    @SingleTask(name = PULL_APOLLO_APP, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, OpenAppDTO entity) {
        return AppConverter.toAssetContainer(dsInstance, entity);
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfName()
                .compareOfKey()
                .compareOfKey2()
                .build();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(apolloAppProvider);
    }

}
