package com.baiyi.opscloud.tencent.exmail.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.TencentExmailConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.tencent.exmail.convert.ExmailAssetConvert;
import com.baiyi.opscloud.tencent.exmail.datasource.TencentExmailUserDatasource;
import com.baiyi.opscloud.tencent.exmail.entity.ExmailUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.common.constant.SingleTaskConstants.PULL_TENCENT_EXMAIL_USER;
import static com.baiyi.opscloud.tencent.exmail.datasource.TencentExmailUserDatasource.ALL_DEPARTMENT;

/**
 * @Author baiyi
 * @Date 2021/10/14 11:01 上午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TencentExmailUserProvider extends BaseAssetProvider<ExmailUser> {

    @Resource
    private TencentExmailUserProvider tencentExmailUserProvider;

    private final TencentExmailUserDatasource tencentExmailUserDatasource;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.TENCENT_EXMAIL.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.TENCENT_EXMAIL_USER.getType();
    }

    private TencentExmailConfig.Tencent buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, TencentExmailConfig.class).getTencent();
    }

    @Override
    protected List<ExmailUser> listEntities(DsInstanceContext dsInstanceContext) {
        TencentExmailConfig.Tencent tencent = buildConfig(dsInstanceContext.getDsConfig());
        return tencentExmailUserDatasource.list(tencent, ALL_DEPARTMENT);
    }

    @Override
    @SingleTask(name = PULL_TENCENT_EXMAIL_USER, lockTime = "2m")
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
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, ExmailUser entity) {
        return ExmailAssetConvert.toAssetContainer(dsInstance, entity);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(tencentExmailUserProvider);
    }
}
