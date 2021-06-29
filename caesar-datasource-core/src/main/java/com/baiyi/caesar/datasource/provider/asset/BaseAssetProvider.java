package com.baiyi.caesar.datasource.provider.asset;

import com.baiyi.caesar.datasource.builder.AssetContainer;
import com.baiyi.caesar.datasource.factory.DsConfigFactory;
import com.baiyi.caesar.datasource.model.DsInstanceContext;
import com.baiyi.caesar.datasource.provider.base.asset.SimpleAssetProvider;
import com.baiyi.caesar.datasource.provider.base.common.SimpleDsInstanceProvider;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.baiyi.caesar.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.caesar.service.datasource.DsInstanceAssetService;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/19 4:22 下午
 * @Version 1.0
 */
public abstract class BaseAssetProvider<T> extends SimpleDsInstanceProvider implements SimpleAssetProvider, InitializingBean {

    @Resource
    protected DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsInstanceAssetPropertyService dsInstanceAssetPropertyService;

    @Resource
    protected DsConfigFactory dsFactory;

    protected abstract List<T> listEntries(DsInstanceContext dsInstanceContext);

    private void enterAssets(DsInstanceContext dsInstanceContext, List<T> entries) {
        entries.forEach(e -> enterEntry(dsInstanceContext, e));
    }

    protected void enterEntry(DsInstanceContext dsInstanceContext, T entry) {
        enterAsset(toAssetContainer(dsInstanceContext.getDsInstance(), entry));
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
            try {
                dsInstanceAssetService.add(preAsset);
            }catch (Exception e){
                e.printStackTrace();
            }
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
        DsInstanceContext dsInstanceContext = buildDsInstanceContext(dsInstanceId);
        List<T> entries = listEntries(dsInstanceContext);
        enterAssets(dsInstanceContext, entries);
    }

}
