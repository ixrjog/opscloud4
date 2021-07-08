package com.baiyi.opscloud.datasource.provider.asset;

import com.baiyi.opscloud.datasource.builder.AssetContainer;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.provider.annotation.ChildProvider;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.google.common.collect.Sets;
import org.springframework.aop.support.AopUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/8 2:56 下午
 * @Since 1.0
 */
public abstract class AbstractChildAssetProvider<C> extends BaseAssetProvider<C> {

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Override
    protected List<C> listEntries(DsInstanceContext dsInstanceContext) {
        throw new UnsupportedOperationException();
    }

    private String getParentAssetKey() {
        return AopUtils.getTargetClass(this).getAnnotation(ChildProvider.class).parentType().getType();
    }

    protected abstract List<C> listEntries(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset asset);

    protected List<DatasourceInstanceAsset> listParents(DsInstanceContext dsInstanceContext) {
        return dsInstanceAssetService.listByInstanceAssetType(dsInstanceContext.getDsInstance().getUuid(), getParentAssetKey());
    }

    @Override
    public void doPull(int dsInstanceId) {
        DsInstanceContext dsInstanceContext = buildDsInstanceContext(dsInstanceId);
        List<DatasourceInstanceAsset> parents = listParents(dsInstanceContext);
        parents.forEach(p -> {
            List<C> entries = listEntries(dsInstanceContext, p);
            enterAssets(dsInstanceContext, entries, p);
        });
    }

    private void enterAssets(DsInstanceContext dsInstanceContext, List<C> entries, DatasourceInstanceAsset parent) {
        if (executeMode()) {
            Set<Integer> idSet = listAssetsIdSet(dsInstanceContext, parent);
            entries.forEach(e -> enterEntry(dsInstanceContext, idSet, e, parent));
            idSet.forEach(id -> baseDsAssetFacade.deleteAssetById(id));
        } else {
            entries.forEach(e -> enterEntry(dsInstanceContext, e));
        }
    }

    private void enterEntry(DsInstanceContext dsInstanceContext, Set<Integer> idSet, C entry, DatasourceInstanceAsset parent) {
        DatasourceInstanceAsset asset = enterEntry(dsInstanceContext, entry, parent);
        idSet.remove(asset.getId());
    }

    private DatasourceInstanceAsset enterEntry(DsInstanceContext dsInstanceContext, C entry, DatasourceInstanceAsset parent) {
        AssetContainer assetContainer = toAssetContainer(dsInstanceContext.getDsInstance(), entry);
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
