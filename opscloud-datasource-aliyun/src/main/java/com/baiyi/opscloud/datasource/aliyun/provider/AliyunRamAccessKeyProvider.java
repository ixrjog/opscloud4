package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.ram.model.v20150501.ListAccessKeysResponse;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.datasource.aliyun.convert.RamAssetConvert;
import com.baiyi.opscloud.datasource.aliyun.ram.handler.AliyunRamHandler;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.ChildProvider;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetChildProvider;
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

import static com.baiyi.opscloud.common.constant.SingleTaskConstants.PULL_ALIYUN_RAM_ACCESS_KEY;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/8 2:46 下午
 * @Since 1.0
 */

@Component
@ChildProvider(parentType = DsAssetTypeEnum.RAM_USER)
public class AliyunRamAccessKeyProvider extends AbstractAssetChildProvider<ListAccessKeysResponse.AccessKey> {

    @Resource
    private AliyunRamHandler aliyunRamHandler;

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
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, ListAccessKeysResponse.AccessKey entry) {
        return RamAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        return preAsset.getIsActive().equals(asset.getIsActive());
    }

    @Override
    protected List<ListAccessKeysResponse.AccessKey> listEntries(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset asset) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds()))
            return Collections.emptyList();
        List<ListAccessKeysResponse.AccessKey> accessKeyList = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> accessKeyList.addAll(aliyunRamHandler.listAccessKeys(regionId, aliyun, asset.getAssetKey())));
        return accessKeyList;
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