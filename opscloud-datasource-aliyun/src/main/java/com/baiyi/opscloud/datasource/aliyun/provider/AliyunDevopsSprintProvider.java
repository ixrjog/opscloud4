package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyun.sdk.service.devops20210625.models.ListSprintsResponseBody;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunDevopsConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.ChildProvider;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetChildProvider;
import com.baiyi.opscloud.datasource.aliyun.converter.DevopsAssetConverter;
import com.baiyi.opscloud.datasource.aliyun.devops.driver.AliyunDevopsSprintsDriver;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/5/12 10:30
 * @Version 1.0
 */
@Component
@ChildProvider(parentType = DsAssetTypeConstants.ALIYUN_DEVOPS_PROJECT)
public class AliyunDevopsSprintProvider extends AbstractAssetChildProvider<ListSprintsResponseBody.Sprints> {

    @Resource
    private AliyunDevopsSprintProvider aliyunDevopsSprintProvider;

    @Override
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunDevopsConfig.Devops buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, AliyunDevopsConfig.class).getDevops();
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        return true;
    }

    @Override
    protected List<ListSprintsResponseBody.Sprints> listEntities(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset asset) {
        AliyunDevopsConfig.Devops devops = buildConfig(dsInstanceContext.getDsConfig());
        return AliyunDevopsSprintsDriver.listSprints(devops.getRegionId(), devops, "Project", asset.getAssetId());
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