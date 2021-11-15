package com.baiyi.opscloud.nexus.provider;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.NexusDsInstanceConfig;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.nexus.convert.NexusAssetConvert;
import com.baiyi.opscloud.nexus.entry.NexusAsset;
import com.baiyi.opscloud.nexus.handler.NexusAssetHandler;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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
    private NexusAssetHandler nexusAssetHandler;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.NEXUS.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.NEXUS_ASSET.getType();
    }

    private NexusDsInstanceConfig.Nexus buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, NexusDsInstanceConfig.class).getNexus();
    }

    @Override
    protected List<NexusAsset.Item> listEntries(DsInstanceContext dsInstanceContext) {
        NexusDsInstanceConfig.Nexus nexus = buildConfig(dsInstanceContext.getDsConfig());
        List<NexusAsset.Item> entries = Lists.newArrayList();
        nexus.getRepositories().forEach(r -> {
            String continuationToken = "";
            while (true) {
                NexusAsset.Assets assets = nexusAssetHandler.list(nexus, r.getName(), continuationToken);
                if (assets == null || CollectionUtils.isEmpty(assets.getItems()))
                    return;
                System.out.println(JSON.toJSONString(assets ));
                entries.addAll(filter(nexus, assets.getItems()));
                System.out.println(JSON.toJSONString("Size: " +entries.size()));
                continuationToken = assets.getContinuationToken();
            }
        });
        return entries;
    }

    private List<NexusAsset.Item> filter(NexusDsInstanceConfig.Nexus nexus, List<NexusAsset.Item> items) {
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
    @SingleTask(name = "pull_nexus_asset", lockTime = "1m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        if (preAsset.getIsActive() != asset.getIsActive())
            return false;
        return true;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, NexusAsset.Item entry) {
        return NexusAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(nexusAssetProvider);
    }
}

