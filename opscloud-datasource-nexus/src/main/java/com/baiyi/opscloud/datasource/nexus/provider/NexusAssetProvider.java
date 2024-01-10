package com.baiyi.opscloud.datasource.nexus.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.NexusConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.nexus.driver.NexusAssetDriver;
import com.baiyi.opscloud.datasource.nexus.entity.NexusAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_NEXUS_ASSET;

/**
 * @Author baiyi
 * @Date 2021/10/18 10:43 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class NexusAssetProvider extends BaseAssetProvider<NexusAsset.Item> {

    @Resource
    private NexusAssetProvider nexusAssetProvider;

    @Resource
    private NexusAssetDriver nexusAssetDriver;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.NEXUS.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.NEXUS_ASSET.name();
    }

    private NexusConfig.Nexus buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, NexusConfig.class).getNexus();
    }

    @Override
    protected List<NexusAsset.Item> listEntities(DsInstanceContext dsInstanceContext) {
        NexusConfig.Nexus nexus = buildConfig(dsInstanceContext.getDsConfig());
        List<NexusAsset.Item> entities = Lists.newArrayList();
        nexus.getRepositories().forEach(r -> {
            String continuationToken = "";
            while (true) {
                NexusAsset.Assets assets = nexusAssetDriver.list(nexus, r.getName(), continuationToken);
                if (assets == null || CollectionUtils.isEmpty(assets.getItems()))
                    return;
                entities.addAll(filter(nexus, assets.getItems()));
                continuationToken = assets.getContinuationToken();
            }
        });
        return entities;
    }

    private List<NexusAsset.Item> filter(NexusConfig.Nexus nexus, List<NexusAsset.Item> items) {
        return items.stream().filter(i -> {
            for (String s : nexus.getFilter()) {
                if (i.getPath().endsWith(s)) {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
    }

    @Override
    @SingleTask(name = PULL_NEXUS_ASSET, lockTime = "1m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfName()
                .compareOfActive()
                .build();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(nexusAssetProvider);
    }

}