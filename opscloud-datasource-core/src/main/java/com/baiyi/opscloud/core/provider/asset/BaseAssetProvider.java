package com.baiyi.opscloud.core.provider.asset;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.base.asset.SimpleAssetProvider;
import com.baiyi.opscloud.core.provider.base.common.SimpleDsInstanceProvider;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.facade.datasource.SimpleDsAssetFacade;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.sys.CredentialService;
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
public abstract class BaseAssetProvider<T> extends SimpleDsInstanceProvider implements SimpleAssetProvider<T>, InitializingBean {

    @Resource
    protected DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsInstanceAssetPropertyService dsInstanceAssetPropertyService;

    @Resource
    protected SimpleDsAssetFacade simpleDsAssetFacade;

    @Resource
    private CredentialService credentialService;

    @Resource
    protected DsConfigHelper dsConfigHelper;

    public interface Model {
        boolean INCREMENT = false; // 增量模式: 不删除旧数据
        boolean SYNC = true;       // 同步模式: 删除旧数据
    }

    protected boolean executeMode() {
        return Model.SYNC;
    }

    protected abstract List<T> listEntities(DsInstanceContext dsInstanceContext);

    private void enterAssets(DsInstanceContext dsInstanceContext, List<T> entities) {
        if (executeMode()) {
            Set<Integer> idSet = listAssetsIdSet(dsInstanceContext);
            entities.forEach(e -> enterEntity(dsInstanceContext, idSet, e));
            idSet.forEach(id -> simpleDsAssetFacade.deleteAssetById(id));
        } else {
            entities.forEach(e -> enterEntity(dsInstanceContext, e));
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

    protected void enterEntity(DsInstanceContext dsInstanceContext, Set<Integer> idSet, T entity) {
        DatasourceInstanceAsset asset = enterEntity(dsInstanceContext, entity);
        filterAsset(idSet, asset.getId());
    }

    private void filterAsset(Set<Integer> idSet, Integer id) {
        idSet.remove(id);
    }

    protected DatasourceInstanceAsset enterEntity(DsInstanceContext dsInstanceContext, T entity) {
        return enterAsset(toAssetContainer(dsInstanceContext.getDsInstance(), entity));
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
            if (!equals(asset, preAsset)) {
                dsInstanceAssetService.update(preAsset);
            }
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

    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, T entity) {
        if (entity instanceof IToAsset) {
            return ((IToAsset) entity).toAssetContainer(dsInstance);
        }
        throw new UnsupportedOperationException("资产类型转换错误！");
    }

    protected void doPull(int dsInstanceId) {
        DsInstanceContext dsInstanceContext = buildDsInstanceContext(dsInstanceId);
        List<T> entities = listEntities(dsInstanceContext);
        enterAssets(dsInstanceContext, entities);
    }

    /**
     * PULL单个资产
     *
     * @param dsInstanceId
     * @param entity
     * @return
     */
    @Override
    public DatasourceInstanceAsset pullAsset(int dsInstanceId, T entity) {
        DsInstanceContext dsInstanceContext = buildDsInstanceContext(dsInstanceId);
        return enterEntity(dsInstanceContext, entity);
    }

    @Override
    public void pushAsset(int dsInstanceId) {
    }

    protected Credential getCredential(int credentialId) {
        return credentialService.getById(credentialId);
    }
}
