package com.baiyi.opscloud.core.provider.asset;

import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.base.common.ITargetProvider;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetRelation;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2021/6/19 6:58 下午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractAssetRelationProvider<S, T> extends AbstractAssetBusinessRelationProvider<S> implements ITargetProvider {

    @Resource
    private DsInstanceAssetRelationService dsInstanceAssetRelationService;

    protected abstract List<S> listEntities(DsInstanceContext dsInstanceContext, T target);

    private AbstractAssetRelationProvider<T, S> getTargetProvider() {
        List<AbstractAssetRelationProvider<T, S>> providers = AssetProviderFactory.getProviders(getInstanceType(), getTargetAssetKey());
        assert providers != null;
        Optional<AbstractAssetRelationProvider<T, S>> optional = providers.stream()
                .filter(e -> e.getTargetAssetKey().equals(this.getAssetType()))
                .findFirst();
        return optional.orElse(null);
    }

    protected List<T> listTarget(DsInstanceContext dsInstanceContext, S source) {
        AbstractAssetRelationProvider<T, S> targetAssetProvider = getTargetProvider();
        if (targetAssetProvider == null) return Collections.emptyList();
        return targetAssetProvider.listEntities(dsInstanceContext, source);
    }

    @Override
    protected DatasourceInstanceAsset enterEntity(DsInstanceContext dsInstanceContext, S entity) {
        DatasourceInstanceAsset asset = super.enterAsset(toAssetContainer(dsInstanceContext.getDsInstance(), entity));
        List<T> targets = listTarget(dsInstanceContext, entity);
        if (!CollectionUtils.isEmpty(targets)) {
            targets.forEach(target ->
                    enterEntity(dsInstanceContext, asset, target)
            );
        }
        return asset;
    }

    private void enterEntity(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset asset, T target) {
        // 目标关系生产者
        AbstractAssetRelationProvider<T, S> targetAssetProvider = getTargetProvider();
        AssetContainer assetContainer = targetAssetProvider.toAssetContainer(dsInstanceContext.getDsInstance(), target);
        DatasourceInstanceAsset targetAsset = dsInstanceAssetService.getByUniqueKey(assetContainer.getAsset());
        if (targetAsset == null) return;
//        if (targetAsset == null) {
//            UniqueAssetParam param = UniqueAssetParam.builder()
//                    .assetId(assetContainer.getAsset().getAssetId())
//                    .build();
//            try {
//                targetAsset = targetAssetProvider.doPull(dsInstanceContext.getDsInstance().getId(), param);
//            } catch (Exception e) {
//                log.info(e.getMessage());
//                return;
//            }
//        }
        DatasourceInstanceAssetRelation relation = DatasourceInstanceAssetRelation.builder()
                .instanceUuid(dsInstanceContext.getDsInstance().getUuid())
                .sourceAssetId(asset.getId())
                .targetAssetId(targetAsset.getId())
                .relationType(getTargetAssetKey())
                .build();
        enterRelation(relation);
    }

    private void enterRelation(DatasourceInstanceAssetRelation relation) {
        dsInstanceAssetRelationService.save(relation);
    }

}
