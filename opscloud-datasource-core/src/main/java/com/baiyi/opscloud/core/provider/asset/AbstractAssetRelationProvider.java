package com.baiyi.opscloud.core.provider.asset;

import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.base.common.ITargetProvider;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetRelation;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetRelationService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/19 6:58 下午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractAssetRelationProvider<S, T> extends AbstractAssetBusinessRelationProvider<S> implements ITargetProvider {

    @Resource
    private DsInstanceAssetRelationService dsInstanceAssetRelationService;

    /**
     * 返回与目标资产建立关系的源资产
     *
     * @param dsInstanceContext 数据源实例上下文
     * @param target            目标资产
     * @return
     */
    protected abstract List<S> listEntities(DsInstanceContext dsInstanceContext, T target);

    private AbstractAssetRelationProvider<T, S> getTargetProvider() {
        List<AbstractAssetRelationProvider<T, S>> providers = AssetProviderFactory.getProviders(getInstanceType(), getTargetAssetKey());
        assert providers != null;
        return providers.stream().filter(e -> e.getTargetAssetKey().equals(this.getAssetType())).findFirst().orElse(null);
    }

    /**
     * 返回与源资产建立关系的目标资产
     *
     * @param dsInstanceContext 数据源实例上下文
     * @param source            源资产
     * @return
     */
    protected List<T> listTarget(DsInstanceContext dsInstanceContext, S source) {
        AbstractAssetRelationProvider<T, S> targetAssetProvider = getTargetProvider();
        if (targetAssetProvider == null) {
            return Collections.emptyList();
        }
        return targetAssetProvider.listEntities(dsInstanceContext, source);
    }

    @Override
    protected DatasourceInstanceAsset enterEntity(DsInstanceContext dsInstanceContext, S entity) {
        DatasourceInstanceAsset asset = super.enterAsset(toAssetContainer(dsInstanceContext.getDsInstance(), entity));
        List<T> targets = listTarget(dsInstanceContext, entity);
        // 获取建立的关系列表
        List<DatasourceInstanceAssetRelation> validRelations = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(targets)) {
            targets.forEach(target -> {
                DatasourceInstanceAssetRelation relation = enterEntity(dsInstanceContext, asset, target);
                if (relation != null) {
                    validRelations.add(relation);
                }
            });
        }
        clearInvalidRelations(dsInstanceContext, asset, validRelations);
        return asset;
    }

    /**
     * 清除无效的关系
     *
     * @param dsInstanceContext
     * @param asset
     * @param validRelations
     */
    private void clearInvalidRelations(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset asset, List<DatasourceInstanceAssetRelation> validRelations) {
        Map<Integer, DatasourceInstanceAssetRelation> relationMap = validRelations.stream()
                .collect(Collectors.toMap(DatasourceInstanceAssetRelation::getId, a -> a, (k1, k2) -> k1));
        List<DatasourceInstanceAssetRelation> invalidRelations = dsInstanceAssetRelationService.queryTargetAsset(dsInstanceContext.getDsInstance().getUuid(), asset.getId())
                .stream()
                .filter(e -> !relationMap.containsKey(e.getId())).toList();
        if (CollectionUtils.isEmpty(invalidRelations)) {
            return;
        }
        invalidRelations.forEach(this::clearRelation);
    }

    private void clearRelation(DatasourceInstanceAssetRelation relation) {
        log.info("删除无效的资产绑定关系: datasourceInstanceAssetRelationId={}", relation.getId());
        dsInstanceAssetRelationService.deleteById(relation.getId());
    }

    private DatasourceInstanceAssetRelation enterEntity(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset asset, T target) {
        // 目标关系生产者
        AbstractAssetRelationProvider<T, S> targetAssetProvider = getTargetProvider();
        AssetContainer assetContainer = targetAssetProvider.toAssetContainer(dsInstanceContext.getDsInstance(), target);
        DatasourceInstanceAsset targetAsset = dsInstanceAssetService.getByUniqueKey(assetContainer.getAsset());
        if (targetAsset == null) {
            return null;
        }
        DatasourceInstanceAssetRelation relation = DatasourceInstanceAssetRelation.builder()
                .instanceUuid(dsInstanceContext.getDsInstance().getUuid())
                .sourceAssetId(asset.getId())
                .targetAssetId(targetAsset.getId())
                .relationType(getTargetAssetKey()).build();
        return enterRelation(relation);
    }

    private DatasourceInstanceAssetRelation enterRelation(DatasourceInstanceAssetRelation relation) {
        return dsInstanceAssetRelationService.save(relation);
    }

}