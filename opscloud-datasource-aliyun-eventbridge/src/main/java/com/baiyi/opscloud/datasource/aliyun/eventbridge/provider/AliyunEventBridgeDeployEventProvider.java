package com.baiyi.opscloud.datasource.aliyun.eventbridge.provider;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunEventBridgeConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.aliyun.eventbridge.entity.LeoDeployEvent;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/8/31 11:02
 * @Version 1.0
 */
@Component
public class AliyunEventBridgeDeployEventProvider extends BaseAssetProvider<LeoDeployEvent> {

    @Resource
    private AliyunEventBridgeDeployEventProvider aliyunEventBridgeDeployEventProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN_EVENTBRIDGE.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.EVENT_BRIDGE_DEPLOY_EVENT.name();
    }

    private AliyunEventBridgeConfig.EventBridge buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AliyunEventBridgeConfig.class).getEventBridge();
    }

    @Override
    protected List<LeoDeployEvent> listEntities(DsInstanceContext dsInstanceContext) {
        return Collections.emptyList();
    }

    @Override
    public void pullAsset(int dsInstanceId) {
        // TODO 不适用
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset a1, DatasourceInstanceAsset a2) {
        return false;
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunEventBridgeDeployEventProvider);
    }

}