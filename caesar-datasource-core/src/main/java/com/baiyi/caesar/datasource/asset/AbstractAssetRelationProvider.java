package com.baiyi.caesar.datasource.asset;

import com.baiyi.caesar.datasource.builder.AssetContainer;
import com.baiyi.caesar.datasource.common.ITargetProvider;
import com.baiyi.caesar.datasource.factory.AssetProviderFactory;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAssetRelation;
import com.baiyi.caesar.service.datasource.DsInstanceAssetRelationService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/19 6:58 下午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractAssetRelationProvider<S, T> extends BaseAssetProvider<S> implements ITargetProvider {

    @Resource
    private DsInstanceAssetRelationService dsInstanceAssetRelationService;

    protected abstract List<S> listEntries(DatasourceConfig dsConfig, T target);

    private AbstractAssetRelationProvider<T, S> getTargetProvider() {
        return AssetProviderFactory.getProvider(getInstanceType(), getTargetAssetKey());
    }

    protected List<T> listTarget(DatasourceConfig dsConfig, S source) {
        AbstractAssetRelationProvider<T, S> targetAssetProvider = getTargetProvider();
        if (targetAssetProvider == null) return Lists.newArrayList();
        return targetAssetProvider.listEntries(dsConfig, source);
    }

    @Override
    protected void enterEntry(DatasourceInstance dsInstance, DatasourceConfig dsConfig, S source) {
        DatasourceInstanceAsset asset = super.enterAsset(toAssetContainer(dsInstance, source));
        List<T> targets = listTarget(dsConfig, source);
        targets.forEach(target -> {
            AbstractAssetRelationProvider<T, S> targetAssetProvider = getTargetProvider();
            AssetContainer assetContainer = targetAssetProvider.toAssetContainer(dsInstance, target);
            DatasourceInstanceAsset targetAsset = dsInstanceAssetService.getByUniqueKey(assetContainer.getAsset());
            if (targetAsset == null) return; // 关联对象未录入
            DatasourceInstanceAssetRelation relation = DatasourceInstanceAssetRelation.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .sourceAssetId(asset.getId())
                    .targetAssetId(targetAsset.getId())
                    .relationType(getTargetAssetKey())
                    .build();
            enterRelation(relation);
        });
    }

    private void enterRelation(DatasourceInstanceAssetRelation relation) {
        dsInstanceAssetRelationService.save(relation);
    }

}
