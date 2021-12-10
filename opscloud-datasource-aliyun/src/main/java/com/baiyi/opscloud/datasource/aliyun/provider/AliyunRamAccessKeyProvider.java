package com.baiyi.opscloud.datasource.aliyun.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.ChildProvider;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetChildProvider;
import com.baiyi.opscloud.datasource.aliyun.convert.RamAssetConvert;
import com.baiyi.opscloud.datasource.aliyun.ram.drive.AliyunRamAccessKeyDrive;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.AccessKey;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_RAM_ACCESS_KEY;

/**
 * @Author 修远
 * @Date 2021/7/8 2:46 下午
 * @Since 1.0
 */

@Component
@ChildProvider(parentType = DsAssetTypeEnum.RAM_USER)
public class AliyunRamAccessKeyProvider extends AbstractAssetChildProvider<AccessKey.Key> {

    @Resource
    private AliyunRamAccessKeyDrive aliyunRamAccessKeyDrive;

    @Resource
    private AliyunRamAccessKeyProvider aliyunRamAccessKeyProvider;

    @Override
    @SingleTask(name = PULL_ALIYUN_RAM_ACCESS_KEY)
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, AliyunConfig.class).getAliyun();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, AccessKey.Key entity) {
        return RamAssetConvert.toAssetContainer(dsInstance, entity);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        return preAsset.getIsActive().equals(asset.getIsActive());
    }

    @Override
    protected List<AccessKey.Key> listEntities(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset asset) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds()))
            return Collections.emptyList();
        List<AccessKey.Key> entities = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> entities.addAll(aliyunRamAccessKeyDrive.listAccessKeys(regionId, aliyun, asset.getAssetKey())));
        return entities;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.RAM_ACCESS_KEY.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunRamAccessKeyProvider);
    }
}