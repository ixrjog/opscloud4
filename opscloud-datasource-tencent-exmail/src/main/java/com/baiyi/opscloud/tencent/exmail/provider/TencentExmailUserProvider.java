package com.baiyi.opscloud.tencent.exmail.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.TencentExmailConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.tencent.exmail.driver.TencentExmailUserDriver;
import com.baiyi.opscloud.tencent.exmail.entity.ExmailUser;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_TENCENT_EXMAIL_USER;
import static com.baiyi.opscloud.tencent.exmail.driver.TencentExmailUserDriver.ALL_DEPARTMENT;

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

    private final TencentExmailUserDriver tencentExmailUserDriver;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.TENCENT_EXMAIL.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.TENCENT_EXMAIL_USER.name();
    }

    private TencentExmailConfig.Tencent buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, TencentExmailConfig.class).getTencent();
    }

    @Override
    protected List<ExmailUser> listEntities(DsInstanceContext dsInstanceContext) {
        TencentExmailConfig.Tencent tencent = buildConfig(dsInstanceContext.getDsConfig());
        return tencentExmailUserDriver.list(tencent, ALL_DEPARTMENT);
    }

    @Override
    @SingleTask(name = PULL_TENCENT_EXMAIL_USER, lockTime = "2m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfName()
                .compareOfActive()
                .build();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(tencentExmailUserProvider);
    }

}