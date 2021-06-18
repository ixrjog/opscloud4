package com.baiyi.caesar.datasource.compute;

import com.baiyi.caesar.datasource.common.IDsProvider;
import com.baiyi.caesar.datasource.common.IElasticComputeProvider;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.baiyi.caesar.service.datasource.DsConfigService;
import com.baiyi.caesar.service.datasource.DsInstanceAssetService;
import com.baiyi.caesar.service.datasource.DsInstanceService;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/18 2:49 下午
 * @Version 1.0
 */
public abstract class AbstractComputeProvider<T> implements IElasticComputeProvider, IDsProvider, InitializingBean {

    @Resource
    private DsInstanceService dsInstanceService;

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    protected abstract List<T> listInstance(DatasourceConfig dsConfig);

    private void enterComputeAssets(DatasourceInstance dsInstance, List<T> computeInstances) {
        computeInstances.forEach(e -> {
            DatasourceInstanceAsset preAsset = toAsset(dsInstance, e);
            enterComputeAsset(preAsset);
        });
    }

    private void enterComputeAsset(DatasourceInstanceAsset preAsset) {
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
            if (!equals(asset, preAsset))
                dsInstanceAssetService.update(preAsset);
        }
    }

    /**
     * 判断资产是否更新
     *
     * @param asset
     * @param preAsset
     * @return
     */
    protected abstract boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset);

    protected abstract DatasourceInstanceAsset toAsset(DatasourceInstance dsInstance, T compute);

    @Override
    public void pullAsset(int dsInstanceId) {
        DatasourceInstance dsInstance = dsInstanceService.getById(dsInstanceId);
        DatasourceConfig dsConfig = dsConfigService.getById(dsInstance.getConfigId());
        List<T> computeInstances = listInstance(dsConfig);
        enterComputeAssets(dsInstance, computeInstances);
    }

}
