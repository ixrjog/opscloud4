package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyun.sdk.service.devops20210625.models.ListSprintsResponseBody;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunDevopsConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.aliyun.converter.DevopsAssetConverter;
import com.baiyi.opscloud.datasource.aliyun.devops.driver.AliyunDevopsSprintsDriver;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_DEVOPS_SPRINT;

/**
 * @Author baiyi
 * @Date 2023/5/12 10:30
 * @Version 1.0
 */
@Component
public class AliyunDevopsSprintProvider extends BaseAssetProvider<ListSprintsResponseBody.Sprints> {

    @Resource
    private AliyunDevopsSprintProvider aliyunDevopsSprintProvider;

    @Override
    @SingleTask(name = PULL_ALIYUN_DEVOPS_SPRINT, lockTime = "10m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunDevopsConfig.Devops buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AliyunDevopsConfig.class).getDevops();
    }

    @Override
    protected List<ListSprintsResponseBody.Sprints> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunDevopsConfig.Devops devops = buildConfig(dsInstanceContext.getDsConfig());
        List<ListSprintsResponseBody.Sprints> entities = Lists.newArrayList();
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
            entities.addAll(AliyunDevopsSprintsDriver.listSprints(devops.getRegionId(), devops, "Project", projectAsset.getAssetId()));
        });
        return entities;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN_DEVOPS.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ALIYUN_DEVOPS_SPRINT.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunDevopsSprintProvider);
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, ListSprintsResponseBody.Sprints entity) {
        return DevopsAssetConverter.toAssetContainer(dsInstance, entity);
    }

}