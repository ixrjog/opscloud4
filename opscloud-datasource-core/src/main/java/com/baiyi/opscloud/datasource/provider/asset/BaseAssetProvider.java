package com.baiyi.opscloud.datasource.provider.asset;

import com.baiyi.opscloud.datasource.builder.AssetContainer;
import com.baiyi.opscloud.datasource.factory.DsConfigFactory;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.provider.base.asset.SimpleAssetProvider;
import com.baiyi.opscloud.datasource.provider.base.common.SimpleDsInstanceProvider;
import com.baiyi.opscloud.datasource.provider.base.param.AssetFilterParam;
import com.baiyi.opscloud.datasource.provider.base.param.UniqueAssetParam;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.facade.datasource.BaseDsAssetFacade;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    protected BaseDsAssetFacade baseDsAssetFacade;

    @Resource
    protected DsConfigFactory dsFactory;

    public interface Model {
        boolean INCREMENT = false; // 增量模式: 不删除旧数据
        boolean SYNC = true;       // 同步模式: 删除旧数据
    }

    protected boolean executeMode() {
        return Model.SYNC;
    }

    protected abstract List<T> listEntries(DsInstanceContext dsInstanceContext);

    protected T getEntry(DsInstanceContext dsInstanceContext, UniqueAssetParam param) {
        throw new UnsupportedOperationException("该数据源实例不支持单个查询资产");
    }

    protected List<T> listEntries(DsInstanceContext dsInstanceContext, AssetFilterParam param) {
        throw new UnsupportedOperationException("该数据源实例不支持筛选资产");
    }

    private void enterAssets(DsInstanceContext dsInstanceContext, List<T> entries) {
        if (executeMode()) {
            Set<Integer> idSet = listAssetsIdSet(dsInstanceContext);
            entries.forEach(e -> enterEntry(dsInstanceContext, idSet, e));
            idSet.forEach(id -> baseDsAssetFacade.deleteAssetById(id));
        } else {
            entries.forEach(e -> enterEntry(dsInstanceContext, e));
        }
    }

    /**
     * 查询已录入资产
     *
     * @param dsInstanceContext
     * @return
     */
    private List<DatasourceInstanceAsset> listAssets(DsInstanceContext dsInstanceContext) {
        return dsInstanceAssetService.listByInstanceAssetType(dsInstanceContext.getDsInstance().getUuid(), getAssetType());
    }

    private Set<Integer> listAssetsIdSet(DsInstanceContext dsInstanceContext) {
        Set<Integer> idSet = Sets.newHashSet();
        listAssets(dsInstanceContext).forEach(e -> idSet.add(e.getId()));
        return idSet;
    }

    protected void enterEntry(DsInstanceContext dsInstanceContext, Set<Integer> idSet, T entry) {
        DatasourceInstanceAsset asset = enterEntry(dsInstanceContext, entry);
        filterAsset(idSet, asset.getId());
    }

    private void filterAsset(Set<Integer> idSet, Integer id) {
        idSet.remove(id);
    }

    protected DatasourceInstanceAsset enterEntry(DsInstanceContext dsInstanceContext, T entry) {
        return enterAsset(toAssetContainer(dsInstanceContext.getDsInstance(), entry));
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            preAsset.setId(asset.getId());
//            preAsset.setIsActive(asset.getIsActive());
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

    protected DatasourceInstanceAsset doPull(int dsInstanceId, UniqueAssetParam param) {
        DsInstanceContext dsInstanceContext = buildDsInstanceContext(dsInstanceId);
        T entry = getEntry(dsInstanceContext, param);
        return enterEntry(dsInstanceContext, entry);
    }

    protected void doPull(int dsInstanceId, AssetFilterParam filter) {
        DsInstanceContext dsInstanceContext = buildDsInstanceContext(dsInstanceId);
        List<T> entries = listEntries(dsInstanceContext, filter);
        enterAssets(dsInstanceContext, entries);
    }

    @Override
    public DatasourceInstanceAsset pullAsset(int dsInstanceId, UniqueAssetParam param) {
        return doPull(dsInstanceId, param);
    }

    @Override
    public void pullAsset(int dsInstanceId, AssetFilterParam filter) {
        doPull(dsInstanceId, filter);
    }
}
