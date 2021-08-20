package com.baiyi.opscloud.facade.datasource.impl;

import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.datasource.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.factory.SetDsInstanceConfigFactory;
import com.baiyi.opscloud.datasource.provider.base.asset.IAssetBusinessRelation;
import com.baiyi.opscloud.datasource.provider.base.asset.SimpleAssetProvider;
import com.baiyi.opscloud.datasource.provider.base.common.AbstractSetDsInstanceConfigProvider;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import com.baiyi.opscloud.facade.datasource.SimpleDsAssetFacade;
import com.baiyi.opscloud.facade.datasource.DsInstanceFacade;
import com.baiyi.opscloud.packer.datasource.DsAssetPacker;
import com.baiyi.opscloud.packer.datasource.DsInstancePacker;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/11 10:19 上午
 * @Version 1.0
 */
@Service
@Slf4j
public class DsInstanceFacadeImpl implements DsInstanceFacade {

    @Resource
    private DsInstanceService dsInstanceService;

    @Resource
    private DsInstancePacker dsInstancePacker;

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsAssetPacker dsAssetPacker;

    @Resource
    private SimpleDsAssetFacade baseDsAssetFacade;

    @Override
    public DataTable<DsAssetVO.Asset> queryAssetPage(DsAssetParam.AssetPageQuery pageQuery) {
        DatasourceInstance dsInstance = dsInstanceService.getById(pageQuery.getInstanceId());
        pageQuery.setInstanceUuid(dsInstance.getUuid());
        DataTable<DatasourceInstanceAsset> table = dsInstanceAssetService.queryPageByParam(pageQuery);
        return new DataTable<>(dsAssetPacker.wrapVOList(table.getData(), pageQuery, pageQuery), table.getTotalNum());
    }

    @Override
    @Async(value = Global.TaskPools.EXECUTOR)
    public void pullAsset(DsAssetParam.PullAsset pullAsset) {
        DatasourceInstance dsInstance = dsInstanceService.getById(pullAsset.getInstanceId());
        DsInstanceVO.Instance instance = DsInstancePacker.toVO(dsInstance);
        dsInstancePacker.wrap(instance);
        List<SimpleAssetProvider> providers = AssetProviderFactory.getProviders(instance.getInstanceType(), pullAsset.getAssetType());
        assert providers != null;
        providers.forEach(x -> x.pullAsset(pullAsset.getInstanceId()));
    }

    @Override
    public void deleteAssetById(Integer id) {
        baseDsAssetFacade.deleteAssetById(id);
    }

    @Override
    public void setDsInstanceConfig(DsAssetParam.SetDsInstanceConfig setDsInstanceConfig) {
        AbstractSetDsInstanceConfigProvider setDsInstanceConfigProvider = SetDsInstanceConfigFactory.getProvider(setDsInstanceConfig.getInstanceType());
        assert setDsInstanceConfigProvider != null;
        setDsInstanceConfigProvider.setConfig(setDsInstanceConfig.getInstanceId());
    }

    @Override
    @Async
    public void scanAssetBusiness(DsAssetParam.ScanAssetBusiness scanAssetBusiness) {
        DatasourceInstance dsInstance = dsInstanceService.getById(scanAssetBusiness.getInstanceId());
        DsInstanceVO.Instance instance = DsInstancePacker.toVO(dsInstance);
        dsInstancePacker.wrap(instance);
        List<SimpleAssetProvider> providers = AssetProviderFactory.getProviders(instance.getInstanceType(), scanAssetBusiness.getAssetType());
        assert providers != null;
        for (SimpleAssetProvider provider : providers) {
            if (provider instanceof IAssetBusinessRelation) {
                ((IAssetBusinessRelation) provider).scan(scanAssetBusiness.getInstanceId());
            }
        }
    }
}
