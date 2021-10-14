package com.baiyi.opscloud.tencent.exmail.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.TencentExmailDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsTencentExmailConfig;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.util.AssetUtil;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.tencent.exmail.convert.ExmailAssetConvert;
import com.baiyi.opscloud.tencent.exmail.entry.ExmailUser;
import com.baiyi.opscloud.tencent.exmail.handler.TencentExmailUserHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.tencent.exmail.handler.TencentExmailUserHandler.ALL_DEPARTMENT;

/**
 * @Author baiyi
 * @Date 2021/10/14 11:01 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class TencentExmailUserProvider extends BaseAssetProvider<ExmailUser> {

    @Resource
    private TencentExmailUserProvider tencentExmailUserProvider;

    @Resource
    private TencentExmailUserHandler tencentExmailUserHandler;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.TENCENT_EXMAIL.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.TENCENT_EXMAIL_USER.getType();
    }

    private DsTencentExmailConfig.Tencent buildConfig(DatasourceConfig dsConfig) {
        return dsConfigFactory.build(dsConfig, TencentExmailDsInstanceConfig.class).getTencent();
    }

    @Override
    protected List<ExmailUser> listEntries(DsInstanceContext dsInstanceContext) {
        DsTencentExmailConfig.Tencent tencent = buildConfig(dsInstanceContext.getDsConfig());
        return tencentExmailUserHandler.list(tencent, ALL_DEPARTMENT);
    }

    @Override
    @SingleTask(name = "pull_tencent_exmail_user", lockTime = "2m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        if (preAsset.getIsActive() != asset.getIsActive())
            return false;
        return true;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, ExmailUser entry) {
        return ExmailAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(tencentExmailUserProvider);
    }
}
