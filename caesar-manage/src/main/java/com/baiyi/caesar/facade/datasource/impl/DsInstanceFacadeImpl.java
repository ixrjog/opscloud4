package com.baiyi.caesar.facade.datasource.impl;

import com.baiyi.caesar.common.base.Global;
import com.baiyi.caesar.datasource.base.asset.SimpleAssetProvider;
import com.baiyi.caesar.datasource.factory.AssetProviderFactory;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.annotation.TagClear;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.baiyi.caesar.domain.param.datasource.DsAssetParam;
import com.baiyi.caesar.domain.types.BusinessTypeEnum;
import com.baiyi.caesar.domain.vo.datasource.DsAssetVO;
import com.baiyi.caesar.domain.vo.datasource.DsInstanceVO;
import com.baiyi.caesar.facade.datasource.DsInstanceFacade;
import com.baiyi.caesar.packer.datasource.DsAssetPacker;
import com.baiyi.caesar.packer.datasource.DsInstancePacker;
import com.baiyi.caesar.service.datasource.DsInstanceAssetRelationService;
import com.baiyi.caesar.service.datasource.DsInstanceAssetService;
import com.baiyi.caesar.service.datasource.DsInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    private DsInstanceAssetRelationService dsInstanceAssetRelationService;

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
        SimpleAssetProvider simpleAssetProvider = AssetProviderFactory.getProvider(instance.getInstanceType(), pullAsset.getAssetType());
        assert simpleAssetProvider != null;
        simpleAssetProvider.pullAsset(pullAsset.getInstanceId());
    }


    @TagClear(type = BusinessTypeEnum.ASSET)
    @Override
    public void deleteAssetById(Integer id) {
        // 删除关联关系
        dsInstanceAssetRelationService.queryByAssetId(id).forEach(e ->
                dsInstanceAssetRelationService.deleteById(e.getId())
        );
        dsInstanceAssetService.deleteById(id);
    }
}
