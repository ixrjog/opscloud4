package com.baiyi.caesar.datasource.asset;

import com.baiyi.caesar.datasource.builder.AssetContainer;
import com.baiyi.caesar.datasource.common.SimpleAssetProvider;
import com.baiyi.caesar.datasource.factory.DsConfigFactory;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.baiyi.caesar.service.datasource.DsConfigService;
import com.baiyi.caesar.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.caesar.service.datasource.DsInstanceAssetService;
import com.baiyi.caesar.service.datasource.DsInstanceService;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/19 4:22 下午
 * @Version 1.0
 */
public abstract class BaseAssetProvider<T> implements SimpleAssetProvider, InitializingBean {

    @Resource
    private DsInstanceService dsInstanceService;

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    protected DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsInstanceAssetPropertyService dsInstanceAssetPropertyService;

    @Resource
    protected DsConfigFactory dsFactory;

    protected abstract List<T> listEntries(DatasourceConfig dsConfig);

    private void enterAssets(DatasourceInstance dsInstance, DatasourceConfig dsConfig, List<T> entries) {
        entries.forEach(e -> enterEntry(dsInstance, dsConfig, e));
    }

    protected void enterEntry(DatasourceInstance dsInstance, DatasourceConfig dsConfig, T entry) {
        enterAsset(toAssetContainer(dsInstance, entry));
    }

    protected DatasourceInstanceAsset enterAsset(AssetContainer assetContainer) {
        DatasourceInstanceAsset asset = enterAsset(assetContainer.getAsset());
        enterAssetProperties(assetContainer.getAsset().getId(), assetContainer.getProperties());
        return asset;
    }

    private void enterAssetProperties(int assetId, Map<String, String> properties) {
        dsInstanceAssetPropertyService.saveAssetProperties(assetId, properties);
    }

    private DatasourceInstanceAsset enterAsset(DatasourceInstanceAsset preAsset) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(preAsset.getInstanceUuid())
                .assetId(preAsset.getAssetId())
                .assetType(preAsset.getAssetType())
                .assetKey(preAsset.getAssetKey())
                .build();
        asset = dsInstanceAssetService.getByUniqueKey(asset);
        if (asset == null) {
            dsInstanceAssetService.add(preAsset);
        } else {
            preAsset.setId(asset.getId());
            preAsset.setIsActive(asset.getIsActive());
            if (!equals(asset, preAsset))
                dsInstanceAssetService.update(preAsset);
        }
        return preAsset;
    }

    /**
     * 判断资产是否更新
     *
     * @param asset
     * @param preAsset
     * @return
     */
    protected abstract boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset);

    protected abstract AssetContainer toAssetContainer(DatasourceInstance dsInstance, T entry);

    protected void doPull(int dsInstanceId) {
        DatasourceInstance dsInstance = dsInstanceService.getById(dsInstanceId);
        DatasourceConfig dsConfig = dsConfigService.getById(dsInstance.getConfigId());
        List<T> entries = listEntries(dsConfig);
        enterAssets(dsInstance, dsConfig, entries);
    }


}
