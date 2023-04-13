package com.baiyi.opscloud.core.provider.asset;

import com.baiyi.opscloud.common.exception.asset.ListEntitiesException;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.ChildProvider;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.google.common.collect.Sets;
import org.springframework.aop.support.AopUtils;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @Author 修远
 * @Date 2021/7/8 2:56 下午
 * @Since 1.0
 */
public abstract class AbstractAssetChildProvider<C> extends BaseAssetProvider<C> {

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Override
    protected List<C> listEntities(DsInstanceContext dsInstanceContext) throws ListEntitiesException {
        throw new UnsupportedOperationException();
    }

    private String getParentAssetKey() {
        return AopUtils.getTargetClass(this).getAnnotation(ChildProvider.class).parentType().name();
    }

    /**
     * 父资产查询子条目
     * @param dsInstanceContext
     * @param parentAsset
     * @return
     * @throws ListEntitiesException
     */
    protected abstract List<C> listEntities(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset parentAsset) throws ListEntitiesException;

    protected List<DatasourceInstanceAsset> listParents(DsInstanceContext dsInstanceContext) {
        return dsInstanceAssetService.listByInstanceAssetType(dsInstanceContext.getDsInstance().getUuid(), getParentAssetKey());
    }

    @Override
    public void doPull(int dsInstanceId) {
        DsInstanceContext dsInstanceContext = buildDsInstanceContext(dsInstanceId);
        List<DatasourceInstanceAsset> parents = listParents(dsInstanceContext);
        parents.forEach(p -> {
            List<C> entities = listEntities(dsInstanceContext, p);
            enterAssets(dsInstanceContext, entities, p);
        });
    }

    private void enterAssets(DsInstanceContext dsInstanceContext, List<C> entities, DatasourceInstanceAsset parent) {
        if (executeMode()) {
            Set<Integer> idSet = listAssetsIdSet(dsInstanceContext, parent);
            entities.forEach(e -> enterEntity(dsInstanceContext, idSet, e, parent));
            idSet.forEach(id -> simpleDsAssetFacade.deleteAssetById(id));
        } else {
            entities.forEach(e -> enterEntity(dsInstanceContext, e));
        }
    }

    private void enterEntity(DsInstanceContext dsInstanceContext, Set<Integer> idSet, C entity, DatasourceInstanceAsset parent) {
        DatasourceInstanceAsset asset = enterEntity(dsInstanceContext, entity, parent);
        idSet.remove(asset.getId());
    }

    private DatasourceInstanceAsset enterEntity(DsInstanceContext dsInstanceContext, C entity, DatasourceInstanceAsset parent) {
        AssetContainer assetContainer = toAssetContainer(dsInstanceContext.getDsInstance(), entity);
        assetContainer.getAsset().setParentId(parent.getId());
        return enterAsset(assetContainer);
    }

    protected Set<Integer> listAssetsIdSet(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset parent) {
        Set<Integer> idSet = Sets.newHashSet();
        listAssets(dsInstanceContext, parent).forEach(e -> idSet.add(e.getId()));
        return idSet;
    }

    private List<DatasourceInstanceAsset> listAssets(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset parent) {
        return dsInstanceAssetService.listByUuidParentIdAssetAndType(dsInstanceContext.getDsInstance().getUuid(), parent.getId(), getAssetType());
    }

}