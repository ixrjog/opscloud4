package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyun.sdk.service.arms20190808.models.ListTraceAppsResponseBody;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunArmsConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.aliyun.arms.driver.AliyunArmsTraceAppDriver;
import com.baiyi.opscloud.datasource.aliyun.converter.ArmsAssetConverter;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_ARMS_TRACE_APP;

/**
 * @Author baiyi
 * @Date 2023/6/27 10:56
 * @Version 1.0
 */
@Component
public class AliyunArmsTraceAppProvider extends BaseAssetProvider<ListTraceAppsResponseBody.TraceApps> {

    @Resource
    private AliyunArmsTraceAppProvider aliyunArmsTraceAppProvider;

    @Override
    @SingleTask(name = PULL_ALIYUN_ARMS_TRACE_APP, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunArmsConfig.Arms buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AliyunArmsConfig.class).getArms();
    }

    @Override
    protected List<ListTraceAppsResponseBody.TraceApps> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunArmsConfig.Arms arms = buildConfig(dsInstanceContext.getDsConfig());
        return AliyunArmsTraceAppDriver.listTraceApps(arms.getRegionId(), arms);
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN_ARMS.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ALIYUN_ARMS_TRACE_APP.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunArmsTraceAppProvider);
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, ListTraceAppsResponseBody.TraceApps entity) {
        return  ArmsAssetConverter.toAssetContainer(dsInstance, entity);
    }

}