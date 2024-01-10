package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyun.sdk.service.devops20210625.models.ListProjectsResponseBody;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunDevopsConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.aliyun.converter.DevopsAssetConverter;
import com.baiyi.opscloud.datasource.aliyun.devops.driver.AliyunDevopsProjectDriver;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_DEVOPS_PROJECT;

/**
 * @Author baiyi
 * @Date 2023/5/12 09:36
 * @Version 1.0
 */
@Component
public class AliyunDevopsProjectProvider extends BaseAssetProvider<ListProjectsResponseBody.Projects> {

    @Resource
    private AliyunDevopsProjectProvider aliyunDevopsProjectProvider;

    @Override
    @SingleTask(name = PULL_ALIYUN_DEVOPS_PROJECT, lockTime = "10m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunDevopsConfig.Devops buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AliyunDevopsConfig.class).getDevops();
    }

    @Override
    protected boolean executeMode() {
        return Model.INCREMENT;
    }

    @Override
    protected List<ListProjectsResponseBody.Projects> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunDevopsConfig.Devops devops = buildConfig(dsInstanceContext.getDsConfig());
        return AliyunDevopsProjectDriver.listProjects(devops.getRegionId(), devops);
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN_DEVOPS.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ALIYUN_DEVOPS_PROJECT.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunDevopsProjectProvider);
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, ListProjectsResponseBody.Projects entity) {
        return DevopsAssetConverter.toAssetContainer(dsInstance, entity);
    }

}