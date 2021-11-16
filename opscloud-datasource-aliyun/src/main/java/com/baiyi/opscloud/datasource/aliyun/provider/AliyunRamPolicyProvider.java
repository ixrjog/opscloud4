package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.ram.model.v20150501.ListPoliciesForUserResponse;
import com.aliyuncs.ram.model.v20150501.ListPoliciesResponse;
import com.aliyuncs.ram.model.v20150501.ListUsersResponse;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.datasource.aliyun.convert.RamAssetConvert;
import com.baiyi.opscloud.datasource.aliyun.ram.AliyunRamDatasource;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
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
import java.util.stream.Collectors;

import static com.baiyi.opscloud.common.constant.SingleTaskConstants.PULL_ALIYUN_RAM_POLICY;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/2 8:15 下午
 * @Since 1.0
 */

@Component
public class AliyunRamPolicyProvider extends AbstractAssetRelationProvider<ListPoliciesResponse.Policy, ListUsersResponse.User> {

    @Resource
    private AliyunRamDatasource aliyunRamDatasource;

    @Resource
    private AliyunRamPolicyProvider aliyunRamPolicyProvider;

    @Override
    @SingleTask(name = PULL_ALIYUN_RAM_POLICY, lockTime = "2m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, AliyunConfig.class).getAliyun();
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
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds()))
            return Collections.emptyList();
        List<ListPoliciesResponse.Policy> policyList = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> policyList.addAll(aliyunRamDatasource.listPolicies(regionId, aliyun)));
        return policyList;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.RAM_POLICY.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunRamPolicyProvider);
    }

    @Override
    protected List<ListPoliciesResponse.Policy> listEntries(DsInstanceContext dsInstanceContext, ListUsersResponse.User target) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        return aliyunRamDatasource.listPoliciesForUser(aliyun.getRegionId(), aliyun, target.getUserName()).stream().map(this::toTargetEntry
        ).collect(Collectors.toList());
    }

    private ListPoliciesResponse.Policy toTargetEntry(ListPoliciesForUserResponse.Policy policy) {
        ListPoliciesResponse.Policy target = new ListPoliciesResponse.Policy();
        target.setPolicyName(policy.getPolicyName());
        target.setPolicyType(policy.getPolicyType());
        target.setDescription(policy.getDescription());
        target.setDefaultVersion(policy.getDefaultVersion());
        return target;
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeEnum.RAM_USER.name();
    }
}
