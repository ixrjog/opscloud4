package com.baiyi.opscloud.datasource.provider.asset;

import com.baiyi.opscloud.datasource.builder.AssetContainer;
import com.baiyi.opscloud.datasource.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.provider.base.common.ITargetProvider;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetRelation;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetRelationService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Collections;
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

    protected abstract List<S> listEntries(DsInstanceContext dsInstanceContext, T target);

    private AbstractAssetRelationProvider<T, S> getTargetProvider() {
        return AssetProviderFactory.getProvider(getInstanceType(), getTargetAssetKey());
    }

    protected List<T> listTarget(DsInstanceContext dsInstanceContext, S source) {
        AbstractAssetRelationProvider<T, S> targetAssetProvider = getTargetProvider();
        if (targetAssetProvider == null) return Collections.emptyList();
        return targetAssetProvider.listEntries(dsInstanceContext, source);
    }

    @Override
    protected DatasourceInstanceAsset enterEntry(DsInstanceContext dsInstanceContext, S source) {
        DatasourceInstanceAsset asset = super.enterAsset(toAssetContainer(dsInstanceContext.getDsInstance(), source));
        List<T> targets = listTarget(dsInstanceContext, source);
        targets.forEach(target -> {
            AbstractAssetRelationProvider<T, S> targetAssetProvider = getTargetProvider();
            AssetContainer assetContainer = targetAssetProvider.toAssetContainer(dsInstanceContext.getDsInstance(), target);
            DatasourceInstanceAsset targetAsset = dsInstanceAssetService.getByUniqueKey(assetContainer.getAsset());
            if (targetAsset == null) return; // 关联对象未录入
            DatasourceInstanceAssetRelation relation = DatasourceInstanceAssetRelation.builder()
                    .instanceUuid(dsInstanceContext.getDsInstance().getUuid())
                    .sourceAssetId(asset.getId())
                    .targetAssetId(targetAsset.getId())
                    .relationType(getTargetAssetKey())
                    .build();
            enterRelation(relation);
        });
        return asset;
    }

    private void enterRelation(DatasourceInstanceAssetRelation relation) {
        dsInstanceAssetRelationService.save(relation);
    }

}
