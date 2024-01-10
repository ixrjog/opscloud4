package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.exception.DatasourceProviderException;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.aliyun.domain.driver.AliyunDomainDriver;
import com.baiyi.opscloud.datasource.aliyun.domain.entity.AliyunDomain;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_DOMAIN;

/**
 * @Author baiyi
 * @Date 2022/4/18 14:11
 * @Version 1.0
 */
@Slf4j
@Component
public class AliyunDomainProvider extends BaseAssetProvider<AliyunDomain.Domain> {

    @Resource
    private AliyunDomainDriver aliyunDomainDriver;

    @Resource
    private AliyunDomainProvider aliyunDomainProvider;

    @Override
    @SingleTask(name = PULL_ALIYUN_DOMAIN, lockTime = "2m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AliyunConfig.class).getAliyun();
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfKind()
                .compareOfDescription()
                .compareOfExpiredTime()
                .build();
    }

    @Override
    protected List<AliyunDomain.Domain> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds())) {
            return Collections.emptyList();
        }
        try {
            return aliyunDomainDriver.listDomains(aliyun.getRegionId(), aliyun);
        } catch (ClientException e) {
            log.error(e.getMessage());
            throw new DatasourceProviderException(e.getMessage());
        }
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ALIYUN_DOMAIN.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunDomainProvider);
    }

}