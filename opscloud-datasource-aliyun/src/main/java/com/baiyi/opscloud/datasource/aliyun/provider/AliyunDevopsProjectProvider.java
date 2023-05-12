package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyun.sdk.service.devops20210625.models.ListProjectsResponseBody;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunDevopsConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.EnablePullChild;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetBusinessRelationProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.datasource.aliyun.converter.DevopsAssetConverter;
import com.baiyi.opscloud.datasource.aliyun.devops.driver.AliyunDevopsProjectDriver;
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
 * @Date 2023/5/12 09:36
 * @Version 1.0
 */
@Component
public class AliyunDevopsProjectProvider extends AbstractAssetBusinessRelationProvider<ListProjectsResponseBody.Projects> {

    @Resource
    private AliyunDevopsProjectProvider aliyunDevopsProjectProvider;

    @Override
    @EnablePullChild(type = DsAssetTypeConstants.ALIYUN_DEVOPS_PROJECT)
    //@SingleTask(name = PULL_ALIYUN_DEVOPS_PROJECT, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunDevopsConfig.Devops buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, AliyunDevopsConfig.class).getDevops();
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName())) {
            return false;
        }
        if (!AssetUtil.equals(preAsset.getAssetKey2(), asset.getAssetKey2())) {
            return false;
        }
        if (!AssetUtil.equals(preAsset.getDescription(), asset.getDescription())) {
            return false;
        }
        return true;
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
