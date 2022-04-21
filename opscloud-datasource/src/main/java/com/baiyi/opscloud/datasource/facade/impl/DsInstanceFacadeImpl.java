package com.baiyi.opscloud.datasource.facade.impl;

import com.baiyi.opscloud.common.config.ThreadPoolTaskConfiguration;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.factory.SetDsInstanceConfigFactory;
import com.baiyi.opscloud.core.provider.base.asset.IAssetBusinessRelation;
import com.baiyi.opscloud.core.provider.base.asset.SimpleAssetProvider;
import com.baiyi.opscloud.core.provider.base.common.AbstractSetDsInstanceConfigProvider;
import com.baiyi.opscloud.datasource.facade.DsInstanceFacade;
import com.baiyi.opscloud.datasource.packer.DsInstancePacker;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/11 10:19 上午
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DsInstanceFacadeImpl<T> implements DsInstanceFacade<T> {

    private final DsInstanceService dsInstanceService;

    private final DsInstancePacker dsInstancePacker;

    @Override
    @Async(value = ThreadPoolTaskConfiguration.TaskPools.CORE)
    public void pullAsset(DsAssetParam.PullAsset pullAsset) {
        List<SimpleAssetProvider> providers = getProviders(pullAsset.getInstanceId(), pullAsset.getAssetType());
        assert providers != null;
        providers.forEach(x -> x.pullAsset(pullAsset.getInstanceId()));
    }

    @Override
    @Async(value = ThreadPoolTaskConfiguration.TaskPools.CORE)
    public void pushAsset(DsAssetParam.PushAsset pushAsset) {
        List<SimpleAssetProvider> providers = getProviders(pushAsset.getInstanceId(), pushAsset.getAssetType());
        assert providers != null;
        providers.forEach(x -> x.pushAsset(pushAsset.getInstanceId()));
    }

    private List<SimpleAssetProvider> getProviders(Integer instanceId, String assetType) {
        DatasourceInstance dsInstance = dsInstanceService.getById(instanceId);
        DsInstanceVO.Instance instance = BeanCopierUtil.copyProperties(dsInstance, DsInstanceVO.Instance.class);
        dsInstancePacker.wrap(instance, SimpleExtend.EXTEND);
        return AssetProviderFactory.getProviders(instance.getInstanceType(), assetType);
    }

    @Override
    public List<DatasourceInstanceAsset> pullAsset(String instanceUuid, String assetType, T entity) {
        DatasourceInstance dsInstance = dsInstanceService.getByUuid(instanceUuid);
        DsInstanceVO.Instance instance = BeanCopierUtil.copyProperties(dsInstance, DsInstanceVO.Instance.class);
        dsInstancePacker.wrap(instance, SimpleExtend.EXTEND);
        List<SimpleAssetProvider> providers = AssetProviderFactory.getProviders(instance.getInstanceType(), assetType);
        assert providers != null;
        return providers.stream().map(e -> e.pullAsset(instance.getId(), entity)).collect(Collectors.toList())
                .stream().filter(e -> e.getAssetType().equals(assetType)).collect(Collectors.toList());
    }

    @Override
    public void setDsInstanceConfig(DsAssetParam.SetDsInstanceConfig setDsInstanceConfig) {
        AbstractSetDsInstanceConfigProvider setDsInstanceConfigProvider = SetDsInstanceConfigFactory.getProvider(setDsInstanceConfig.getInstanceType());
        assert setDsInstanceConfigProvider != null;
        setDsInstanceConfigProvider.setConfig(setDsInstanceConfig.getInstanceId());
    }

    @Override
    public void scanAssetBusiness(DsAssetParam.ScanAssetBusiness scanAssetBusiness) {
        DatasourceInstance dsInstance = dsInstanceService.getById(scanAssetBusiness.getInstanceId());
        DsInstanceVO.Instance instance = BeanCopierUtil.copyProperties(dsInstance, DsInstanceVO.Instance.class);
        dsInstancePacker.wrap(instance, SimpleExtend.EXTEND);
        List<SimpleAssetProvider> providers = AssetProviderFactory.getProviders(instance.getInstanceType(), scanAssetBusiness.getAssetType());
        assert providers != null;
        for (SimpleAssetProvider provider : providers) {
            if (provider instanceof IAssetBusinessRelation) {
                ((IAssetBusinessRelation) provider).scan(scanAssetBusiness.getInstanceId());
            }
        }
    }

}
