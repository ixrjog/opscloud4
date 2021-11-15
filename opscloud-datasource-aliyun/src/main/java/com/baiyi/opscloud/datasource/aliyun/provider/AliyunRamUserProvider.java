package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.ram.model.v20150501.ListEntitiesForPolicyResponse;
import com.aliyuncs.ram.model.v20150501.ListPoliciesResponse;
import com.aliyuncs.ram.model.v20150501.ListUsersResponse;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.datasource.aliyun.convert.RamAssetConvert;
import com.baiyi.opscloud.datasource.aliyun.ram.handler.AliyunRamHandler;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.EnablePullChild;
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

import static com.baiyi.opscloud.common.constant.SingleTaskConstants.PULL_ALIYUN_RAM_USER;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/2 7:46 下午
 * @Since 1.0
 */
@Component
public class AliyunRamUserProvider extends AbstractAssetRelationProvider<ListUsersResponse.User, ListPoliciesResponse.Policy> {

    @Resource
    private AliyunRamHandler aliyunRamHandler;

    @Resource
    private AliyunRamUserProvider aliyunRamUserProvider;

    @Override
    @EnablePullChild(type = DsAssetTypeEnum.RAM_USER)
    @SingleTask(name = PULL_ALIYUN_RAM_USER, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, AliyunConfig.class).getAliyun();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, ListUsersResponse.User entry) {
        return RamAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        if (!AssetUtil.equals(preAsset.getAssetKey2(), asset.getAssetKey2()))
            return false;
        if (!AssetUtil.equals(preAsset.getDescription(), asset.getDescription()))
            return false;
        return true;
    }

    @Override
    protected List<ListUsersResponse.User> listEntries(DsInstanceContext dsInstanceContext) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds()))
            return Collections.emptyList();
        List<ListUsersResponse.User> userList = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> userList.addAll(aliyunRamHandler.listUsers(regionId, aliyun)));
        return userList;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.RAM_USER.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunRamUserProvider);
    }

    @Override
    protected List<ListUsersResponse.User> listEntries(DsInstanceContext dsInstanceContext, ListPoliciesResponse.Policy target) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        return aliyunRamHandler.listUsersForPolicy(aliyun.getRegionId(), aliyun, target.getPolicyType(), target.getPolicyName())
                .stream().map(this::toTargetEntry)
                .collect(Collectors.toList());
    }

    private ListUsersResponse.User toTargetEntry(ListEntitiesForPolicyResponse.User user) {
        ListUsersResponse.User target = new ListUsersResponse.User();
        target.setUserName(user.getUserName());
        target.setDisplayName(user.getDisplayName());
        target.setUserId(user.getUserId());
        return target;
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeEnum.RAM_POLICY.name();
    }
}
