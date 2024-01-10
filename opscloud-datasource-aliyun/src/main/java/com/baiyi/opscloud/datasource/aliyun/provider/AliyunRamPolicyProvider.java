package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.datasource.aliyun.ram.driver.AliyunRamPolicyDriver;
import com.baiyi.opscloud.datasource.aliyun.ram.driver.AliyunRamUserDriver;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamPolicy;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamUser;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_RAM_POLICY;

/**
 * @Author 修远
 * @Date 2021/7/2 8:15 下午
 * @Since 1.0
 */
@Slf4j
@Component
public class AliyunRamPolicyProvider extends AbstractAssetRelationProvider<RamPolicy.Policy, RamUser.User> {

    @Resource
    private AliyunRamPolicyDriver aliyunRamPolicyDriver;

    @Resource
    private AliyunRamUserDriver aliyunRamUserDriver;

    @Resource
    private AliyunRamPolicyProvider aliyunRamPolicyProvider;

    @Override
    @SingleTask(name = PULL_ALIYUN_RAM_POLICY, lockTime = "2m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AliyunConfig.class).getAliyun();
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfAssetId()
                .compareOfKey()
                .compareOfDescription()
                .build();
    }

    @Override
    protected List<RamPolicy.Policy> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds())) {
            return Collections.emptyList();
        }
        List<RamPolicy.Policy> entities = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> {
            try {
                entities.addAll(aliyunRamPolicyDriver.listPolicies(regionId, aliyun));
            } catch (ClientException e) {
                log.error("查询AliyunRAM策略错误！");
            }
        });
        return entities;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.RAM_POLICY.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunRamPolicyProvider);
    }

    @Override
    protected List<RamPolicy.Policy> listEntities(DsInstanceContext dsInstanceContext, RamUser.User target) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        return aliyunRamUserDriver.listPoliciesForUser(aliyun.getRegionId(), aliyun, target.getUserName());
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeConstants.RAM_USER.name();
    }

}