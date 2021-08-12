package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.ram.model.v20150501.ListPoliciesResponse;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.AliyunDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsAliyunConfig;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.aliyun.convert.RamAssetConvert;
import com.baiyi.opscloud.datasource.aliyun.ram.handler.AliyunRamHandler;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.datasource.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.util.AssetUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/2 8:15 下午
 * @Since 1.0
 */

@Component
public class AliyunRamPolicyProvider extends BaseAssetProvider<ListPoliciesResponse.Policy> {

    @Resource
    private AliyunRamHandler aliyunRamHandler;

    @Resource
    private AliyunRamPolicyProvider aliyunRamPolicyProvider;

    @Override
    @SingleTask(name = "PullAliyunRamPolicy", lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private DsAliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigFactory.build(dsConfig, AliyunDsInstanceConfig.class).getAliyun();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, ListPoliciesResponse.Policy entry) {
        return RamAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getAssetId(), asset.getAssetId()))
            return false;
        if (!AssetUtil.equals(preAsset.getAssetKey(), asset.getAssetKey()))
            return false;
        if (!AssetUtil.equals(preAsset.getDescription(), asset.getDescription()))
            return false;
        return true;
    }

    @Override
    protected List<ListPoliciesResponse.Policy> listEntries(DsInstanceContext dsInstanceContext) {
        DsAliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds()))
            return Collections.emptyList();
        List<ListPoliciesResponse.Policy> policyList = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> policyList.addAll(aliyunRamHandler.listPolicies(regionId, aliyun)));
        return policyList;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.RAM_POLICY.getType();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunRamPolicyProvider);
    }
}
