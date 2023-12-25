package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyun.sdk.service.devops20210625.models.ListWorkitemsResponseBody;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunDevopsConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.aliyun.converter.DevopsAssetConverter;
import com.baiyi.opscloud.datasource.aliyun.devops.driver.AliyunDevopsWorkitemsDriver;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_DEVOPS_WORKITEM;

/**
 * @Author baiyi
 * @Date 2023/5/12 10:59
 * @Version 1.0
 */
@Component
public class AliyunDevopsWorkitemProvider extends BaseAssetProvider<ListWorkitemsResponseBody.Workitems> {

    @Resource
    private AliyunDevopsWorkitemProvider aliyunDevopsWorkitemProvider;

    @Override
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunDevopsConfig.Devops buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AliyunDevopsConfig.class).getDevops();
    }

    @Override
    @SingleTask(name = PULL_ALIYUN_DEVOPS_WORKITEM, lockTime = "10m")
    protected List<ListWorkitemsResponseBody.Workitems> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunDevopsConfig.Devops devops = buildConfig(dsInstanceContext.getDsConfig());
        List<ListWorkitemsResponseBody.Workitems> entities = Lists.newArrayList();

        List<String> categories = Optional.of(devops)
                .map(AliyunDevopsConfig.Devops::getSyncOptions)
                .map(AliyunDevopsConfig.SyncOptions::getWorkitem)
                .map(AliyunDevopsConfig.Workitem::getCategories)
                .orElse(Collections.emptyList());

        if (CollectionUtils.isEmpty(categories)) {
            return entities;
        }
        // query all project assets
        DatasourceInstanceAsset query = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstanceContext.getDsInstance().getUuid())
                .assetType(DsAssetTypeConstants.ALIYUN_DEVOPS_PROJECT.name())
                .build();
        List<DatasourceInstanceAsset> projectAssets = dsInstanceAssetService.queryAssetByAssetParam(query);
        if (CollectionUtils.isEmpty(projectAssets)) {
            return entities;
        }
        // projectAsset.getAssetId() 是 projectId
        projectAssets.forEach(projectAsset -> {
            categories.forEach(category ->
                    entities.addAll(AliyunDevopsWorkitemsDriver.listWorkitems(devops.getRegionId(), devops, projectAsset.getAssetId(), "Project", category))
            );
        });
        return entities;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN_DEVOPS.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ALIYUN_DEVOPS_WORKITEM.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunDevopsWorkitemProvider);
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, ListWorkitemsResponseBody.Workitems entity) {
        return DevopsAssetConverter.toAssetContainer(dsInstance, entity);
    }

}