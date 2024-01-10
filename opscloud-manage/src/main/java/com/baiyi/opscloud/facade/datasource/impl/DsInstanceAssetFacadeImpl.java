package com.baiyi.opscloud.facade.datasource.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.param.datasource.DsInstanceParam;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import com.baiyi.opscloud.facade.datasource.DsFacade;
import com.baiyi.opscloud.facade.datasource.DsInstanceAssetFacade;
import com.baiyi.opscloud.facade.datasource.SimpleDsAssetFacade;
import com.baiyi.opscloud.packer.datasource.DsAssetPacker;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/7/5 11:01 上午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class DsInstanceAssetFacadeImpl implements DsInstanceAssetFacade {

    private final DsFacade dsFacade;

    private final DsInstanceAssetService dsInstanceAssetService;

    private final SimpleDsAssetFacade simpleDsAssetFacade;

    private final DsAssetPacker dsAssetPacker;

    private final DsInstanceService dsInstanceService;

    @Override
    public DataTable<DsAssetVO.Asset> queryAssetPage(DsAssetParam.AssetPageQuery pageQuery) {
        DatasourceInstance dsInstance = dsInstanceService.getById(pageQuery.getInstanceId());
        pageQuery.setInstanceUuid(dsInstance.getUuid());
        DataTable<DatasourceInstanceAsset> table = dsInstanceAssetService.queryPageByParam(pageQuery);
        List<DsAssetVO.Asset> data = BeanCopierUtil.copyListProperties(table.getData(), DsAssetVO.Asset.class)
                .stream()
                .peek(e -> dsAssetPacker.wrap(e, pageQuery, pageQuery)
                ).collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public List<DsAssetVO.Asset> querySshKeyAssets(String username) {
        DsInstanceParam.DsInstanceQuery instanceQuery = DsInstanceParam.DsInstanceQuery.builder()
                .instanceType(DsTypeEnum.GITLAB.getName())
                .extend(false)
                .build();
        List<DsInstanceVO.Instance> instances = dsFacade.queryDsInstance(instanceQuery);
        List<DsAssetVO.Asset> result = Lists.newArrayList();
        instances.forEach(i -> {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .assetType(DsAssetTypeConstants.GITLAB_SSHKEY.name())
                    .instanceUuid(i.getUuid())
                    .name(username)
                    .build();
            List<DatasourceInstanceAsset> sshKeyAssets = dsInstanceAssetService.queryAssetByAssetParam(asset);
            result.addAll(sshKeyAssets.stream().map(a -> dsAssetPacker.wrap(i, a))
                    .toList());
        });
        return result;
    }

    @Override
    public void deleteAssetByAssetId(int assetId) {
        simpleDsAssetFacade.deleteAssetById(assetId);
    }

    @Override
    @Async
    public void deleteAssetByType(int instanceId, String assetType) {
        DatasourceInstance dsInstance = dsInstanceService.getById(instanceId);
        if (dsInstance == null) {
            return;
        }
        DsAssetParam.AssetPageQuery pageQuery = DsAssetParam.AssetPageQuery.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetType(assetType)
                .page(1)
                .length(50)
                .build();
        while (true) {
            DataTable<DatasourceInstanceAsset> table = dsInstanceAssetService.queryPageByParam(pageQuery);
            if (CollectionUtils.isEmpty(table.getData())) {
                return;
            } else {
                try {
                    for (DatasourceInstanceAsset asset : table.getData()) {
                        simpleDsAssetFacade.deleteAssetById(asset.getId());
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }

    @Override
    public void setAssetActiveByAssetId(int assetId) {
        DatasourceInstanceAsset asset = dsInstanceAssetService.getById(assetId);
        if (asset == null) {
            return;
        }
        asset.setIsActive(!asset.getIsActive());
        dsInstanceAssetService.update(asset);
    }

}